package com.tesis.urbe.seccion.dto;

public record NodoCaminoDTO(
        Integer id,
        String tipo, // "LECCION" o "EXAMEN"
        String titulo,
        String descripcion,
        Integer puntos,
        boolean completado,
        boolean desbloqueado
) {}