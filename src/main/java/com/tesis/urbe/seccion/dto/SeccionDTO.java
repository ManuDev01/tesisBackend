package com.tesis.urbe.seccion.dto;

import com.tesis.urbe.seccion.entity.SeccionEntity;

public record SeccionDTO(
        Integer idSeccion,
        Integer idCurso,
        Integer orden,
        String titulo,
        String descripcion,
        String estado,
        Integer puntosSeccion
) {
    public static SeccionDTO fromEntity(SeccionEntity entity) {
        if (entity == null) return null;
        return new SeccionDTO(
                entity.getIdSeccion(),
                entity.getIdCurso(),
                entity.getOrden(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getPuntosSeccion()
        );
    }
}