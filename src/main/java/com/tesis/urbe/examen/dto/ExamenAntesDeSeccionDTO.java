package com.tesis.urbe.examen.dto;

import java.util.List;

public record ExamenAntesDeSeccionDTO(
        Integer idUsuario,
        String usuario,
        String seccion,
        Long leccionesCompletadas,
        Long totalLecciones,
        Long pendientesAlPresentar,
        List<LeccionPendienteDTO> detallePendientes
) {
    public record LeccionPendienteDTO(
            Integer idLeccion,
            String titulo
    ) {}
}