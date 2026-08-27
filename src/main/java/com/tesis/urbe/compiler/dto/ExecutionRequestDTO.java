package com.tesis.urbe.compiler.dto;

public record ExecutionRequestDTO(
        String codigo,
        String entrada // Entradas de texto si el código lee datos por consola (Scanner)
) {}