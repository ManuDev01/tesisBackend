package com.tesis.urbe.compiler.controller;

import com.tesis.urbe.compiler.dto.EvaluationRequestDTO;
import com.tesis.urbe.compiler.dto.ExecutionResultDTO;
import com.tesis.urbe.compiler.service.CompilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compiler")
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/evaluar")
    public ResponseEntity<ExecutionResultDTO> evaluarConCasoDeUso(@RequestBody EvaluationRequestDTO request) {
        ExecutionResultDTO resultado = compilerService.evaluarCodigoConJUnit(request);
        return ResponseEntity.ok(resultado);
    }
}