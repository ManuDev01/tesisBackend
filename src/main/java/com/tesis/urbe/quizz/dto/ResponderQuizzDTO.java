package com.tesis.urbe.quizz.dto;

import java.util.List;

/**
 * Respuestas enviadas por el estudiante para un quiz de una lección.
 * La respuesta debe ser la letra de la opción ("A", "B" o "C").
 *
 * <p>{@code sinFallos} indica que la sesión actual no tuvo ningún fallo
 * (medalla 9: "solo el intento actual"). {@code null} cuando el cliente no
 * envía el campo: en ese caso se conserva la comprobación histórica.
 */
public record ResponderQuizzDTO(
        Integer idUsuario,
        Integer idLeccion,
        List<RespuestaQuizzDTO> respuestas,
        Boolean sinFallos
) {
}
