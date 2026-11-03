package com.tesis.urbe.quizz.dto;

import com.tesis.urbe.medallas.dto.MedallaDTO;

import java.util.List;

public record QuizzResultadoDTO(
        Integer idLeccion,
        int totalPreguntas,
        int respondidas,
        int aciertos,
        int fallos,
        List<DetalleRespuestaDTO> detalle,
        List<MedallaDTO> medallasDesbloqueadas
) {
    public record DetalleRespuestaDTO(
            Integer idQuizz,
            boolean correcta,
            String respuestaEnviada,
            String respuestaCorrecta
    ) {
    }
}
