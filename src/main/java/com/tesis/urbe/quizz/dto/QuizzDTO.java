package com.tesis.urbe.quizz.dto;

import com.tesis.urbe.quizz.entity.QuizzEntity;

public record QuizzDTO(
        Integer idQuizz,
        Integer idLeccion,
        String pregunta,
        String opcionA,
        String opcionB,
        String opcionC,
        String respuestaCorrecta,
        Integer puntosIntento
) {
    public static QuizzDTO fromEntity(QuizzEntity entity) {
        if (entity == null) return null;

        return new QuizzDTO(
                entity.getIdQuizz(),
                entity.getIdLeccion() != null ? entity.getIdLeccion().getIdLeccion() : null,
                entity.getPregunta(),
                entity.getOpcionA(),
                entity.getOpcionB(),
                entity.getOpcionC(),
                entity.getRespuestaCorrecta(),
                entity.getPuntosIntento()
        );
    }
}