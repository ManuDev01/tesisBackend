package com.tesis.urbe.proyectos.dto;

public record EvaluacionRequestDTO(
            String codigo,
            String resultado,
            String modelo,
            String versionPrompt
    ) {}
