package com.tesis.urbe.compiler.service;

import com.tesis.urbe.casosDeUso.entity.CasoDeUsoProyectoCursoEntity;
import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
import com.tesis.urbe.casosDeUso.repository.CasoDeUsoProyectoCursoRepository;
import com.tesis.urbe.casosDeUso.repository.CasosDeUsoRepository;
import com.tesis.urbe.compiler.dto.EvaluationRequestDTO;
import com.tesis.urbe.compiler.dto.ExecutionResultDTO;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@Service
public class CompilerService {

    private final CasosDeUsoRepository casosDeUsoRepository;
    private final CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository;

    public CompilerService(CasosDeUsoRepository casosDeUsoRepository, CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository) {
        this.casosDeUsoRepository = casosDeUsoRepository;
        this.casoDeUsoProyectoCursoRepository = casoDeUsoProyectoCursoRepository;
    }

    private Object[] parsearEntradas(Class<?>[] paramTypes, String entradaRaw) {
        if (paramTypes.length == 0) return new Object[0];

        // Caso 1: Método con firma (int[], int) -> p.ej. Two Sum
        if (paramTypes.length == 2 && paramTypes[0] == int[].class && paramTypes[1] == int.class) {
            // 1. Extraer el contenido dentro de los corchetes [...]
            java.util.regex.Matcher matcherArr = java.util.regex.Pattern.compile("\\[(.*?)\\]").matcher(entradaRaw);
            int[] nums = new int[0];
            if (matcherArr.find()) {
                String arrStr = matcherArr.group(1).trim();
                if (!arrStr.isEmpty()) {
                    nums = Arrays.stream(arrStr.split(","))
                            .map(String::trim)
                            .mapToInt(Integer::parseInt)
                            .toArray();
                }
            }

            // 2. Extraer el último entero de la cadena (que corresponde al target)
            java.util.regex.Matcher matcherTarget = java.util.regex.Pattern.compile("-?\\d+$").matcher(entradaRaw.trim());
            int target = 0;
            if (matcherTarget.find()) {
                target = Integer.parseInt(matcherTarget.group());
            }

            return new Object[]{nums, target};
        }

        // Caso 2: Método con firma (int) -> p.ej. FizzBuzz
        if (paramTypes.length == 1 && paramTypes[0] == int.class) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("-?\\d+").matcher(entradaRaw);
            int val = matcher.find() ? Integer.parseInt(matcher.group()) : 0;
            return new Object[]{val};
        }

        return new Object[]{entradaRaw};
    }

    public ExecutionResultDTO evaluarCodigoConJUnit(EvaluationRequestDTO request) {
        long startTime = System.currentTimeMillis();

        // 1. Detectar dinámicamente la clase enviada por el usuario
        String userClassName = extraerNombreClasePublica(request.codigo());
        if (userClassName == null) {
            return new ExecutionResultDTO(false, "", "Error: No se encontró una clase pública válida.", 0);
        }

        // 2. Inicializar entorno del compilador en memoria
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return new ExecutionResultDTO(false, "", "El JDK no está disponible en el entorno de ejecución.", 0);
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        MemoryClassLoader classLoader = new MemoryClassLoader();

        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager<>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
                return classLoader.getJavaFileObject(className);
            }
        };

        // 3. Obtener casos de uso dinámicamente según el origen (CURSO, LIBRE o PRACTICA)
        List<CasosDeUsoEntity> casosDeUsoList = new ArrayList<>();

        if ("CURSO".equalsIgnoreCase(request.tipoProyecto())) {
            // Cargar desde la tabla casodeusoproyectocurso
            List<CasoDeUsoProyectoCursoEntity> listaCurso =
                    casoDeUsoProyectoCursoRepository.findByIdProyectoCurso(request.idProyecto());

            for (CasoDeUsoProyectoCursoEntity item : listaCurso) {
                CasosDeUsoEntity c = new CasosDeUsoEntity();
                c.setIdCasoDeUso(item.getIdCasoDeUsoProyectoCurso());
                c.setEntrada(item.getEntrada());
                c.setSalidaEsperada(item.getSalidaEsperada());
                c.setEsOculto(item.getEsOculto());
                casosDeUsoList.add(c);
            }

        } else if ("LIBRE".equalsIgnoreCase(request.tipoProyecto()) && request.idProyecto() != null) {
            // Cargar desde la tabla casosdeuso (proyectos libres)
            casosDeUsoList = casosDeUsoRepository.findByIdProyecto_IdProyecto(request.idProyecto());
        }
        // Si tipoProyecto es "PRACTICA" o null, casosDeUsoList queda vacía automáticamente

        // 4. Si no hay casos de uso registrados, ejecutar directamente el método main()
        if (casosDeUsoList.isEmpty()) {
            return ejecutarMainLibre(userClassName, request.codigo(), classLoader, fileManager, compiler, diagnostics, startTime);
        }

        // 5. Generar y compilar la suite de pruebas JUnit si existen casos de uso
        String testClassName = "ProyectoTest";
        String testCode = generarCodigoTestJUnit(testClassName, userClassName, casosDeUsoList);

        JavaFileObject userSourceFile = new JavaSourceFromString(userClassName, request.codigo());
        JavaFileObject testSourceFile = new JavaSourceFromString(testClassName, testCode);
        List<JavaFileObject> compilationUnits = List.of(userSourceFile, testSourceFile);

        List<String> options = List.of("-classpath", obtenerClasspathCompleto());
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean compiled = task.call();

        if (!compiled) {
            StringBuilder errorLog = new StringBuilder("Error de compilación:\n");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errorLog.append(String.format("Línea %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null)));
            }
            return new ExecutionResultDTO(false, "", errorLog.toString(), System.currentTimeMillis() - startTime);
        }

        // 6. Ejecutar la suite mediante JUnit Launcher
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        try {
            System.setOut(new PrintStream(outputStream));
            System.setErr(new PrintStream(outputStream));

            Class<?> userClass = classLoader.loadClass(userClassName);

            // Opcional: Ejecutar main() si el usuario lo incluyó para capturar sus System.out.println
            try {
                Method mainMethod = userClass.getMethod("main", String[].class);
                mainMethod.invoke(null, (Object) new String[]{});
            } catch (NoSuchMethodException ignored) {
                // La clase no tiene main, continúa normalmente con los tests JUnit
            }

            // Ejecutar la suite de pruebas JUnit
            Class<?> testClass = classLoader.loadClass(testClassName);
            LauncherDiscoveryRequest discoveryRequest = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(testClass))
                    .build();

            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.registerTestExecutionListeners(listener);

            ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                launcher.execute(discoveryRequest);
            } finally {
                Thread.currentThread().setContextClassLoader(originalContextClassLoader);
            }

            TestExecutionSummary summary = listener.getSummary();
            long duration = System.currentTimeMillis() - startTime;
            boolean exito = summary.getTestsFailedCount() == 0 && summary.getTestsSucceededCount() > 0;

            StringBuilder feedback = new StringBuilder();
            feedback.append(String.format("Pruebas exitosas: %d/%d\n", summary.getTestsSucceededCount(), summary.getTestsFoundCount()));

            if (!exito) {
                summary.getFailures().forEach(failure -> {
                    String testName = failure.getTestIdentifier().getDisplayName();
                    String mensajeError = failure.getException() != null ? failure.getException().getMessage() : "Error desconocido";
                    feedback.append(String.format("Fallo en [%s]: %s\n", testName, mensajeError));
                });
            }

            return new ExecutionResultDTO(exito, outputStream.toString(), feedback.toString(), duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExecutionResultDTO(false, outputStream.toString(), "Error en ejecución: " + e.getMessage(), duration);
        } finally {
            System.setOut(origOut);
            System.setErr(origErr);
        }
    }

    private ExecutionResultDTO ejecutarMainLibre(
            String userClassName,
            String codigo,
            MemoryClassLoader classLoader,
            JavaFileManager fileManager,
            JavaCompiler compiler,
            DiagnosticCollector<JavaFileObject> diagnostics,
            long startTime) {

        JavaFileObject userSourceFile = new JavaSourceFromString(userClassName, codigo);
        List<String> options = List.of("-classpath", obtenerClasspathCompleto());

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, List.of(userSourceFile));
        boolean compiled = task.call();

        if (!compiled) {
            StringBuilder errorLog = new StringBuilder("Error de compilación:\n");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errorLog.append(String.format("Línea %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null)));
            }
            return new ExecutionResultDTO(false, "", errorLog.toString(), System.currentTimeMillis() - startTime);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        try {
            System.setOut(new PrintStream(outputStream));
            System.setErr(new PrintStream(outputStream));

            Class<?> loadedClass = classLoader.loadClass(userClassName);
            Method mainMethod = loadedClass.getMethod("main", String[].class);

            // Invocar main(new String[]{}) por reflexión
            mainMethod.invoke(null, (Object) new String[]{});

            long duration = System.currentTimeMillis() - startTime;
            return new ExecutionResultDTO(true, outputStream.toString(), "Ejecución finalizada con éxito.", duration);

        } catch (NoSuchMethodException e) {
            return new ExecutionResultDTO(false, "", "Error: La clase no contiene un método public static void main(String[] args).", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            return new ExecutionResultDTO(false, outputStream.toString(), "Error en tiempo de ejecución: " + cause.getMessage(), System.currentTimeMillis() - startTime);
        } finally {
            System.setOut(origOut);
            System.setErr(origErr);
        }
    }

    private String obtenerClasspathCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("java.class.path"));

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl instanceof URLClassLoader urlClassLoader) {
            for (URL url : urlClassLoader.getURLs()) {
                sb.append(File.pathSeparator).append(url.getFile());
            }
        }
        return sb.toString();
    }

    private String extraerNombreClasePublica(String codigoSource) {
        if (codigoSource == null || codigoSource.isBlank()) return null;
        Pattern pattern = Pattern.compile("public\\s+class\\s+([A-Za-z0-9_$]+)");
        Matcher matcher = pattern.matcher(codigoSource);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String generarCodigoTestJUnit(String testClassName, String userClassName, List<CasosDeUsoEntity> casos) {
        StringBuilder sb = new StringBuilder();
        sb.append("import org.junit.jupiter.api.Test;\n");
        sb.append("import static org.junit.jupiter.api.Assertions.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("import java.util.regex.*;\n");
        sb.append("import java.lang.reflect.Method;\n\n");
        sb.append("public class ").append(testClassName).append(" {\n\n");

        for (int i = 0; i < casos.size(); i++) {
            CasosDeUsoEntity caso = casos.get(i);

            String entradaEscapada = caso.getEntrada().trim()
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "")
                    .replace("\n", "\\n");

            String salidaEsperadaEscapada = caso.getSalidaEsperada().trim()
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "")
                    .replace("\n", "\\n");

            sb.append(String.format("""
    @Test
    public void testCaso_%d() throws Exception {
        %s instancia = new %s();
        
        Method targetMethod = Arrays.stream(%s.class.getDeclaredMethods())
                .filter(m -> !m.getName().equals("main"))
                .findFirst()
                .orElse(null);

        assertNotNull(targetMethod, "La clase no contiene un método válido para evaluar.");

        Object resultado = null;
        try {
            Object[] args = parsearEntradasDinamicas(targetMethod.getParameterTypes(), "%s");
            resultado = targetMethod.invoke(instancia, args);
        } catch (Exception e) {
            fail("Error al ejecutar el método: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }

        System.out.println("Caso %d [Entrada: %s] -> Salida: " + formatearSalida(resultado));
        assertResultado("%s", resultado);
    }
    """,
                    i + 1,
                    userClassName, userClassName,
                    userClassName,
                    entradaEscapada,
                    i + 1, entradaEscapada,
                    salidaEsperadaEscapada
            ));
        }

        sb.append("    private Object[] parsearEntradasDinamicas(Class<?>[] paramTypes, String entradaRaw) {\n");
        sb.append("        if (paramTypes.length == 0) return new Object[0];\n");
        sb.append("        List<String> valoresRaw = new ArrayList<>();\n");
        sb.append("        Matcher matcher = Pattern.compile(\"(?:[a-zA-Z0-9_\\\\$]+\\\\s*=\\\\s*)?(?:\\\"([^\\\"]*)\\\"|(\\\\[[^\\\\]]*\\\\])|([^,\\\\n]+))\").matcher(entradaRaw);\n");
        sb.append("        while (matcher.find()) {\n");
        sb.append("            String val = null;\n");
        sb.append("            if (matcher.group(1) != null) val = matcher.group(1);\n");
        sb.append("            else if (matcher.group(2) != null) val = matcher.group(2);\n");
        sb.append("            else if (matcher.group(3) != null) val = matcher.group(3).trim();\n");
        sb.append("            if (val != null && !val.isEmpty()) valoresRaw.add(val);\n");
        sb.append("        }\n");
        sb.append("        Object[] args = new Object[paramTypes.length];\n");
        sb.append("        for (int i = 0; i < paramTypes.length; i++) {\n");
        sb.append("            Class<?> type = paramTypes[i];\n");
        sb.append("            String valStr = i < valoresRaw.size() ? valoresRaw.get(i) : \"0\";\n");
        sb.append("            if (type == String.class) args[i] = valStr;\n");
        sb.append("            else if (type == int.class || type == Integer.class) {\n");
        sb.append("                String cleanNum = valStr.replaceAll(\"[^0-9-]\", \"\");\n");
        sb.append("                args[i] = cleanNum.isEmpty() ? 0 : Integer.parseInt(cleanNum);\n");
        sb.append("            } else if (type == boolean.class || type == Boolean.class) args[i] = Boolean.parseBoolean(valStr);\n");
        sb.append("            else if (type == double.class || type == Double.class) {\n");
        sb.append("                String cleanDouble = valStr.replaceAll(\"[^0-9.-]\", \"\");\n");
        sb.append("                args[i] = cleanDouble.isEmpty() ? 0.0 : Double.parseDouble(cleanDouble);\n");
        sb.append("            } else if (type == int[].class) {\n");
        sb.append("                Matcher arrMatcher = Pattern.compile(\"-?\\\\d+\").matcher(valStr);\n");
        sb.append("                List<Integer> list = new ArrayList<>();\n");
        sb.append("                while (arrMatcher.find()) list.add(Integer.parseInt(arrMatcher.group()));\n");
        sb.append("                args[i] = list.stream().mapToInt(Integer::intValue).toArray();\n");
        sb.append("            } else args[i] = valStr;\n");
        sb.append("        }\n");
        sb.append("        return args;\n");
        sb.append("    }\n\n");

        sb.append("    private String formatearSalida(Object obj) {\n");
        sb.append("        if (obj == null) return \"null\";\n");
        sb.append("        if (obj instanceof int[]) return Arrays.toString((int[]) obj);\n");
        sb.append("        if (obj instanceof Collection<?>) {\n");
        sb.append("            Collection<?> col = (Collection<?>) obj;\n");
        sb.append("            StringBuilder sb = new StringBuilder(\"[\");\n");
        sb.append("            int idx = 0;\n");
        sb.append("            for (Object item : col) {\n");
        sb.append("                sb.append(\"\\\"\").append(item).append(\"\\\"\");\n");
        sb.append("                if (idx < col.size() - 1) sb.append(\",\");\n");
        sb.append("                idx++;\n");
        sb.append("            }\n");
        sb.append("            sb.append(\"]\");\n");
        sb.append("            return sb.toString();\n");
        sb.append("        }\n");
        sb.append("        return obj.toString();\n");
        sb.append("    }\n\n");

        sb.append("    private void assertResultado(String esperada, Object obtenido) {\n");
        sb.append("        String obtenidoStr = formatearSalida(obtenido);\n");
        sb.append("        String expLimpia = esperada.replace(\"\\\\r\\\\n\", \"\\n\").replace(\"\\r\", \"\\n\").trim();\n");
        sb.append("        String obtLimpia = obtenidoStr.replace(\"\\\\r\\\\n\", \"\\n\").replace(\"\\r\", \"\\n\").trim();\n");
        sb.append("        assertEquals(expLimpia, obtLimpia, \"Evaluacion fallida.\\nEsperado:\\n\" + esperada + \"\\nPero se obtuvo:\\n\" + obtenidoStr);\n");
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }
}