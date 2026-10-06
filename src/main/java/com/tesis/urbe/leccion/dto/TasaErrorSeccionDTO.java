package com.tesis.urbe.leccion.dto;

public record TasaErrorSeccionDTO(
        Integer idSeccion,
        String seccion,
        Long fallos,
        Long aciertos,
        Double tasaError
) {}