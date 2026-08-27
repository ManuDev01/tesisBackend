package com.tesis.urbe.compiler.controller;

import com.tesis.urbe.compiler.dto.ExecutionRequestDTO;
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

    @PostMapping("/ejecutar")
    public ResponseEntity<ExecutionResultDTO> ejecutar(@RequestBody ExecutionRequestDTO request) {
        // Asumimos que la clase principal enviada se llama 'Main'
        ExecutionResultDTO resultado = compilerService.ejecutarCodigo(request, "Main");
        return ResponseEntity.ok(resultado);
    }
}