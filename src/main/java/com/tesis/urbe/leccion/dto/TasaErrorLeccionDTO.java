package com.tesis.urbe.leccion.dto;

public record TasaErrorLeccionDTO(
        Integer idLeccion,
        String leccion,
        String seccion,
        Long fallos,
        Long aciertos,
        Double tasaError
) {}