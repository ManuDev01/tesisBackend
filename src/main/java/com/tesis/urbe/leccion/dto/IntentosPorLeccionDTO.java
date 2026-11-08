package com.tesis.urbe.leccion.dto;

/**
 * Conteo de intentos por lección (tabla {@code intento_leccion}), devuelto por
 * {@code GET /leccion/intentos-por-leccion}.
 *
 * <p>Solo incluye lecciones con al menos un intento.
 */
public record IntentosPorLeccionDTO(
        Integer idLeccion,
        String titulo,
        Long totalIntentos
) {}
