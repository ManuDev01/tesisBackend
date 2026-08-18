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
        LocalDate createAt
        ) {
    public static ProyectosDTO fromEntity(ProyectosEntity entity) {
        return new ProyectosDTO(
                entity.getIdProyecto(),
                entity.getIdSeccion() != null ? entity.getIdSeccion().getIdSeccion() : null, // Si idSeccion es una relación JPA
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getCodigo(),
                entity.getEstado(),
                entity.getPuntosProyectos(),
                entity.getIdCasoDeUso() != null ? entity.getIdCasoDeUso().getIdCasoDeUso() : null, // Si idCasoDeUso es una relación JPA
                entity.getCreateAt().toLocalDate()
        );
    }
}