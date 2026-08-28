package com.tesis.urbe.compiler.service;

import com.tesis.urbe.casosDeUso.dto.CasosDeUsoDTO;
import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
import com.tesis.urbe.casosDeUso.repository.CasosDeUsoRepository;
import com.tesis.urbe.compiler.dto.ExecutionResultDTO;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DockerInternalCompilerService {

    private final CasosDeUsoRepository casosDeUsoRepository;

    public DockerInternalCompilerService(CasosDeUsoRepository casosDeUsoRepository) {
        this.casosDeUsoRepository = casosDeUsoRepository;
    }

    public ExecutionResultDTO evaluarConJUnit(ProyectosDTO proyecto, String codigoUsuario) {
        long startTime = System.currentTimeMillis();
        Path tempDir = null;

        try {
            // 1. Obtener la lista de casos de uso asociados desde la BD
// Cambia request.idProyecto() por proyecto.idProyecto() (o la propiedad correspondiente del DTO)
            List<CasosDeUsoEntity> casosDeUsoList = casosDeUsoRepository.findByIdProyecto_IdProyecto(proyecto.idProyecto());

            if (casosDeUsoList.isEmpty()) {
                return new ExecutionResultDTO(false, "", "No se encontraron casos de uso para este proyecto", 0);
            }

// Tomar el primer caso de uso encontrado
            CasosDeUsoDTO casoDeUso = CasosDeUsoDTO.fromEntity(casosDeUsoList.get(0));
            tempDir = Files.createTempDirectory("junit_gradle_eval_");

            // 2. Guardar Solucion.java (Código del estudiante)
            File solucionFile = new File(tempDir.toFile(), "Solucion.java");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(solucionFile))) {
                writer.write(codigoUsuario);
            }

            // 3. Guardar ProyectoTest.java (Test JUnit proveniente de la BD)
            File testFile = new File(tempDir.toFile(), "ProyectoTest.java");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
                writer.write(casoDeUso.entrada()); // <-- Acceso directo al accessor del Record
            }

            // 4. Obtener el Classpath completo manejado por el JVM/Gradle
            String currentClasspath = System.getProperty("java.class.path");

            // 5. Compilar los dos archivos de Java con javac agregando el Classpath
            ProcessBuilder compileProcessBuilder = new ProcessBuilder(
                    "javac",
                    "-cp", currentClasspath,
                    "Solucion.java", "ProyectoTest.java"
            );
            compileProcessBuilder.directory(tempDir.toFile());
            Process compileProcess = compileProcessBuilder.start();

            boolean compiledInTime = compileProcess.waitFor(5, TimeUnit.SECONDS);
            if (!compiledInTime || compileProcess.exitValue() != 0) {
                String errorCompilation = readStream(compileProcess.getErrorStream());
                return new ExecutionResultDTO(false, "", "Error de compilación:\n" + errorCompilation, System.currentTimeMillis() - startTime);
            }

            // 6. Ejecutar ConsoleLauncher de JUnit 5 usando el directorio temporal + Classpath actual
            String fullExecutionClasspath = tempDir.toAbsolutePath() + File.pathSeparator + currentClasspath;

            ProcessBuilder runProcessBuilder = new ProcessBuilder(
                    "java",
                    "-cp", fullExecutionClasspath,
                    "org.junit.platform.console.ConsoleLauncher",
                    "execute",
                    "--select-class", "ProyectoTest",
                    "--details", "none" // Puedes cambiar a 'summary' para más nivel de detalle
            );
            runProcessBuilder.directory(tempDir.toFile());
            Process runProcess = runProcessBuilder.start();

            boolean executionFinished = runProcess.waitFor(5, TimeUnit.SECONDS);
            long duration = System.currentTimeMillis() - startTime;

            if (!executionFinished) {
                runProcess.destroyForcibly();
                return new ExecutionResultDTO(false, "", "Tiempo de ejecución excedido (5s). Revisa si tienes bucles infinitos.", duration);
            }

            String output = readStream(runProcess.getInputStream());
            String errorOutput = readStream(runProcess.getErrorStream());

            // Si el proceso retornó 0 y la consola no reporta fallos, la evaluación es exitosa
            boolean exito = runProcess.exitValue() == 0 && !output.contains("FAILED") && !output.contains("FAILURES");

            return new ExecutionResultDTO(exito, output, errorOutput, duration);

        } catch (Exception e) {
            return new ExecutionResultDTO(false, "", "Error interno del evaluador: " + e.getMessage(), System.currentTimeMillis() - startTime);
        } finally {
            if (tempDir != null) {
                deleteDirectory(tempDir.toFile());
            }
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}