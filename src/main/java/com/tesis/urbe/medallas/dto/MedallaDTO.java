package com.tesis.urbe.medallas.dto;

import com.tesis.urbe.medallas.entity.MedallaEntity;
import java.util.Date;

public record MedallaDTO(
        Integer idMedalla,
        String nombreMedalla,
        String descripcionMedalla,
        Integer valorMedalla,
        Date createAt,
        boolean completado
) {
    public static MedallaDTO fromEntity(MedallaEntity entity) {
        return fromEntity(entity, false);
    }

    public static MedallaDTO fromEntity(MedallaEntity entity, boolean completado) {
        if (entity == null) return null;

        return new MedallaDTO(
                entity.getIdMedalla(),
                entity.getNombreMedalla(),
                entity.getDescripcionMedalla(),
                entity.getValorMedalla(),
                entity.getCreateAt(),
                completado
        );
    }
}