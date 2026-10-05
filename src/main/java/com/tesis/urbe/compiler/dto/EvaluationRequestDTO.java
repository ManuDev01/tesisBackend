package com.tesis.urbe.compiler.dto;

public record EvaluationRequestDTO(
        Integer idProyecto,
        String codigo,
        String tipoProyecto // "CURSO", "LIBRE", o null/blank para práctica libre
) {}