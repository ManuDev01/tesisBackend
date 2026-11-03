package com.tesis.urbe.quizz.dto;

import java.util.List;

/**
 * Respuestas enviadas por el estudiante para un quiz de una lección.
 * La respuesta debe ser la letra de la opción ("A", "B" o "C").
 */
public record ResponderQuizzDTO(
        Integer idUsuario,
        Integer idLeccion,
        List<RespuestaQuizzDTO> respuestas
) {
}
