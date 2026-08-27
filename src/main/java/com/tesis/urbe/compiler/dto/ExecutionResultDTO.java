package com.tesis.urbe.compiler.dto;

public record ExecutionResultDTO(
        boolean exito,
        String salida,
        String error,
        long tiempoEjecucionMs
) {}