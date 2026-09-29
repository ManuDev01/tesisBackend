package com.tesis.urbe.curso.dto;

import com.tesis.urbe.curso.entity.CursoEntity;

public record CursoDTO(
        Integer idCurso,
        String nombre,
        String descripcion
) {
    public static CursoDTO fromEntity(CursoEntity entity) {
        if (entity == null) return null;
        return new CursoDTO(
                entity.getIdCurso(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }
}