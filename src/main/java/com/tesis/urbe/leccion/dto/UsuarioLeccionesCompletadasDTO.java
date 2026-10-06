package com.tesis.urbe.leccion.dto;

import java.util.List;

public record UsuarioLeccionesCompletadasDTO(
        Integer idUsuario,
        String usuario,
        Long leccionesCompletadas,
        List<LeccionDetalleDTO> lecciones
) {
    public record LeccionDetalleDTO(
            Integer idLeccion,
            String titulo
    ) {}
}