package com.tesis.urbe.proyectos.dto;

import com.tesis.urbe.proyectos.entity.ProyectoCursoEntity;
import java.time.LocalDate;

public record ProyectoCursoDTO(
        Integer idProyectoCurso,
        Integer idCurso,
        String titulo,
        String descripcion,
        String codigo,
        String estado,
        Integer puntosProyecto,
        LocalDate createdAt
) {
    public static ProyectoCursoDTO fromEntity(ProyectoCursoEntity entity) {
        if (entity == null) return null;

        return new ProyectoCursoDTO(
                entity.getIdProyectoCurso(),
                entity.getIdCurso(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getCodigo(),
                entity.getEstado(),
                entity.getPuntosProyecto(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toLocalDate() : null
        );
    }
}