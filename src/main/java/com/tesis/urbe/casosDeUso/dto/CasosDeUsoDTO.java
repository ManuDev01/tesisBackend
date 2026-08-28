package com.tesis.urbe.casosDeUso.dto;

import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;

public record CasosDeUsoDTO(
        Integer idCasoDeUso,
        Integer idProyecto,
        String entrada,
        String salidaEsperada,
        Boolean esOculto
) {
    public static CasosDeUsoDTO fromEntity(CasosDeUsoEntity entity) {
        if (entity == null) return null;

        Integer proyectoId = (entity.getIdProyecto() != null)
                ? entity.getIdProyecto().getIdProyecto()
                : null;

        return new CasosDeUsoDTO(
                entity.getIdCasoDeUso(),
                proyectoId,
                entity.getEntrada(),
                entity.getSalidaEsperada(),
                entity.getEsOculto()
        );
    }
}