package com.tesis.urbe.examen.dto;

public record PreguntaExamenDTO(
        Integer idPreguntaExamen,
        Integer idExamen,
        String tipo,
        String enunciado,
        String opcionA,
        String opcionB,
        String opcionC,
        String respuestaCorrecta,
        String codigoInicial,
        String salidaEsperada,
        Integer puntos
) {}