package com.tesis.urbe.proyectos.dto;

import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import java.time.LocalDate;

public record ProyectosDTO(
        Integer idProyecto,
        String titulo,
        String descripcion,
        String codigo,
        String estado,
        Integer puntosProyectos,
        Integer idCasoDeUso,
        LocalDate createdAt,
        boolean completado
) {
    public static ProyectosDTO fromEntity(ProyectosEntity entity, boolean completado) {
        if (entity == null) return null;

        return new ProyectosDTO(
                entity.getIdProyecto(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getCodigo(),
                entity.getEstado(),
                entity.getPuntosProyectos(),
                entity.getCasoDeUso() != null ? entity.getCasoDeUso().getIdCasoDeUso() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toLocalDate() : null,
                completado
        );
    }
}