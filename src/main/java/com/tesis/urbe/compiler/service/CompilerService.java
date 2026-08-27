package com.tesis.urbe.compiler.service;

import com.tesis.urbe.compiler.dto.ExecutionRequestDTO;
import com.tesis.urbe.compiler.dto.ExecutionResultDTO;
import com.tesis.urbe.compiler.service.MemoryClassLoader;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


@Service
public class CompilerService {


    public ExecutionResultDTO ejecutarCodigo(ExecutionRequestDTO request, String className) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return new ExecutionResultDTO(false, "", "El JDK no está disponible en este entorno.", 0);
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        MemoryClassLoader classLoader = new MemoryClassLoader();

        // Configurar el Administrador de Archivos en memoria
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager<>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
                return classLoader.getJavaFileObject(className);
            }
        };

        // 1. Compilar el código
        JavaFileObject file = new JavaSourceFromString(className, request.codigo());
        Iterable<? extends JavaFileObject> compilationUnits = Collections.singletonList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        boolean compiled = task.call();

        if (!compiled) {
            StringBuilder errorLog = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errorLog.append(String.format("Línea %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null)));
            }
            return new ExecutionResultDTO(false, "", errorLog.toString(), 0);
        }

        // 2. Ejecutar el código compilado e interceptar la salida
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;
        InputStream origIn = System.in;

        try {
            System.setOut(new PrintStream(outputStream));
            System.setErr(new PrintStream(errorStream));

            if (request.entrada() != null && !request.entrada().isEmpty()) {
                System.setIn(new ByteArrayInputStream(request.entrada().getBytes()));
            }

            Class<?> loadedClass = classLoader.loadClass(className);
            Method mainMethod = loadedClass.getMethod("main", String[].class);

            // Invocar el método main(String[] args)
            mainMethod.invoke(null, (Object) new String[]{});

            long duration = System.currentTimeMillis() - startTime;
            return new ExecutionResultDTO(true, outputStream.toString(), errorStream.toString(), duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            return new ExecutionResultDTO(false, outputStream.toString(), cause.toString(), duration);
        } finally {
            // Restaurar las salidas por defecto de la consola
            System.setOut(origOut);
            System.setErr(origErr);
            System.setIn(origIn);
        }
    }
}