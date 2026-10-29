package com.tesis.urbe.quizz.dto;

public record QuizzFalloDashboardDTO(
        Integer idQuizz,
        String pregunta,
        String nombreSeccion,
        Long totalFallos
) {
}
