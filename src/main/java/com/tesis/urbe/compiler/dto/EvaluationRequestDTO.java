package com.tesis.urbe.compiler.dto;

public record EvaluationRequestDTO(
        Integer idProyecto,
        Integer idExamen,
        Integer idPreguntaExamen,
        String codigo,
        String entrada,       // 👈 Campo para recibir el texto del usuario
        String tipoProyecto
) {}
