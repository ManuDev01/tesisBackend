package com.tesis.urbe.compiler.service;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

@Service
public class CompilerService {

    private final CasosDeUsoRepository casosDeUsoRepository;

    public CompilerService(CasosDeUsoRepository casosDeUsoRepository) {
        this.casosDeUsoRepository = casosDeUsoRepository;
    }

    /**
     * Extrae las rutas del classpath resolviendo el ClassLoader activo de Spring Boot (incluso en Uber-JAR)
     */
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

    public ExecutionResultDTO evaluarCodigoConJUnit(EvaluationRequestDTO request) {
        long startTime = System.currentTimeMillis();

        List<CasosDeUsoEntity> casosDeUsoList = casosDeUsoRepository.findByIdProyecto_IdProyecto(request.idProyecto());

        if (casosDeUsoList.isEmpty()) {
            return new ExecutionResultDTO(false, "", "No se encontraron casos de uso para este proyecto.", 0);
        }

        // 1. Detectar dinámicamente la clase enviada por el usuario
        String userClassName = extraerNombreClasePublica(request.codigo());
        if (userClassName == null) {
            return new ExecutionResultDTO(false, "", "Error: No se encontró una clase pública válida.", 0);
        }

        // 2. Generar el código fuente de la suite JUnit en tiempo de ejecución
        String testClassName = "ProyectoTest";
        String testCode = generarCodigoTestJUnit(testClassName, userClassName, casosDeUsoList);

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

        // 3. Crear unidades de compilación en memoria
        JavaFileObject userSourceFile = new JavaSourceFromString(userClassName, request.codigo());
        JavaFileObject testSourceFile = new JavaSourceFromString(testClassName, testCode);
        List<JavaFileObject> compilationUnits = List.of(userSourceFile, testSourceFile);

        // 4. Compilar ambas clases con el classpath de la aplicación
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

        // 5. Ejecutar la suite mediante JUnit Launcher
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        try {
            System.setOut(new PrintStream(outputStream));
            System.setErr(new PrintStream(outputStream));

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
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Arrays;\n\n");
        sb.append("public class ").append(testClassName).append(" {\n\n");

        for (int i = 0; i < casos.size(); i++) {
            CasosDeUsoEntity caso = casos.get(i);
            String entrada = caso.getEntrada().trim();

            // Formatear ["1", "2", "Fizz"] -> Arrays.asList("1", "2", "Fizz")
            String salidaEsperadaRaw = caso.getSalidaEsperada().trim();
            String elementos = salidaEsperadaRaw.length() > 2
                    ? salidaEsperadaRaw.substring(1, salidaEsperadaRaw.length() - 1)
                    : "";

            sb.append(String.format("""
            @Test
            public void testCaso_%d() {
                %s instancia = new %s();
                List<String> obtenido = instancia.generarFizzBuzz(%s);
                List<String> esperado = %s;
                
                assertNotNull(obtenido, "El método no debe retornar null");
                assertEquals(esperado, obtenido, "Evaluación fallida para entrada: %s");
            }
            """,
                    i + 1,
                    userClassName,
                    userClassName,
                    entrada,
                    elementos.isEmpty() ? "List.of()" : "Arrays.asList(" + elementos + ")",
                    entrada
            ));
        }

        sb.append("}\n");
        return sb.toString();
    }
}