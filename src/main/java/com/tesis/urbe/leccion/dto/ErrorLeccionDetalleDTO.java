package com.tesis.urbe.leccion.dto;

public record ErrorLeccionDetalleDTO(
        String leccion,
        Long intentosFallidos
) {}