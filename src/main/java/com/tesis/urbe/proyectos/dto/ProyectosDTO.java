package com.tesis.urbe.proyectos.dto;

import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import java.time.LocalDate;

public record ProyectosDTO(
        Integer idProyecto,
        Integer idSeccion,
        String titulo,
        String descripcion,
        String codigo,
        String estado,
        Integer puntosProyectos,
        Integer idCasoDeUso,
        LocalDate createAt,
        String decripcion,
        boolean completado
) {
    public static ProyectosDTO fromEntity(ProyectosEntity entity, boolean completado) {
        if (entity == null) return null;

        return new ProyectosDTO(
                entity.getIdProyecto(),
                entity.getIdSeccion() != null ? entity.getIdSeccion().getIdSeccion() : null,
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getCodigo(),
                entity.getEstado(),
                entity.getPuntosProyectos(),
                entity.getIdCasoDeUso() != null ? entity.getIdCasoDeUso().getIdCasoDeUso() : null,
                entity.getCreateAt() != null ? entity.getCreateAt().toLocalDate() : null,
                entity.getDecripcion(),
                completado
        );
    }
}