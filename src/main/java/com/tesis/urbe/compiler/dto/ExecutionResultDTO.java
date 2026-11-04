package com.tesis.urbe.compiler.dto;

import com.tesis.urbe.medallas.dto.MedallaDTO;

import java.util.List;

/**
 * Resultado de una compilación/evaluación.
 *
 * <p>{@code medallas} contiene las medallas que se desbloquearon con este
 * intento. Si el motor de logros no devolvió nada (o el usuario no envió su
 * identificador) la lista viene vacía: el frontend simplemente no muestra el
 * popup y el resto del flujo continúa normal.
 */
public record ExecutionResultDTO(
        boolean exito,
        String salida,
        String error,
        long tiempoEjecucionMs,
        List<MedallaDTO> medallas
) {

    public ExecutionResultDTO(boolean exito, String salida, String error, long tiempoEjecucionMs) {
        this(exito, salida, error, tiempoEjecucionMs, List.of());
    }
}
