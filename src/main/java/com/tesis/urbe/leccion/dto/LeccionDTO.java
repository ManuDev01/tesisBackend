package com.tesis.urbe.leccion.dto;

import com.tesis.urbe.leccion.entity.LeccionEntity;

public record LeccionDTO(
        Integer idLeccion,
        Integer idSeccion,
        String titulo,
        String descripcion,
        String estado,
        Integer puntosLeccion
) {
    public static LeccionDTO fromEntity(LeccionEntity entity) {
        if (entity == null) return null;

        return new LeccionDTO(
                entity.getIdLeccion(),
                entity.getIdSeccion() != null ? entity.getIdSeccion().getIdSeccion() : null,
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getPuntosLeccion()
        );
    }
}