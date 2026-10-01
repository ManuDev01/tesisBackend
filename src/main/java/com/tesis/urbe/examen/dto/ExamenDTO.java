package com.tesis.urbe.examen.dto;

public record ExamenDTO(
        Integer idExamen,
        Integer idSeccion,
        String titulo,
        String descripcion,
        Integer puntosAumento,
        boolean completado
) {}