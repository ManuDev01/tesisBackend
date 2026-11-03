package com.tesis.urbe.compiler.controller;

import com.tesis.urbe.compiler.dto.EvaluationRequestDTO;
import com.tesis.urbe.compiler.dto.ExecutionResultDTO;
import com.tesis.urbe.compiler.service.CompilerService;
import com.tesis.urbe.medallas.service.LogroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compiler")
public class CompilerController {

    private final CompilerService compilerService;
    private final LogroService logroService;

    public CompilerController(CompilerService compilerService, LogroService logroService) {
        this.compilerService = compilerService;
        this.logroService = logroService;
    }

    @PostMapping("/evaluar")
    public ResponseEntity<ExecutionResultDTO> evaluarConCasoDeUso(@RequestBody EvaluationRequestDTO request) {
        ExecutionResultDTO resultado = compilerService.evaluarCodigoConJUnit(request);

        // Registra el intento y verifica las medallas de compilación.
        logroService.onCompilacion(
                request.idUsuario(),
                request.codigo(),
                request.tipoProyecto(),
                request.idProyecto(),
                compiloCorrectamente(request.codigo(), resultado)
        );

        return ResponseEntity.ok(resultado);
    }

    /**
     * Distingue un fallo de compilación de un fallo de pruebas: para la medalla
     * "Hola Mundo" basta con que el código haya compilado, aunque los tests no
     * pasen.
     */
    private boolean compiloCorrectamente(String codigo, ExecutionResultDTO resultado) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }

        String error = resultado.error();
        if (error == null) {
            return true;
        }

        return !error.startsWith("Error de compilación")
                && !error.startsWith("El JDK")
                && !error.startsWith("Error: No se encontró una clase");
    }
}