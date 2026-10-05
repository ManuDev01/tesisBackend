package com.tesis.urbe.casosDeUso.dto;

import com.tesis.urbe.casosDeUso.entity.CasoDeUsoProyectoCursoEntity;

public record CasoDeUsoProyectoCursoDTO(
        Integer idCasoDeUsoProyectoCurso,
        Integer idProyectoCurso,
        String entrada,
        String salidaEsperada,
        Boolean esOculto
) {
    public static CasoDeUsoProyectoCursoDTO fromEntity(CasoDeUsoProyectoCursoEntity entity) {
        if (entity == null) return null;
        return new CasoDeUsoProyectoCursoDTO(
                entity.getIdCasoDeUsoProyectoCurso(),
                entity.getIdProyectoCurso(),
                entity.getEntrada(),
                entity.getSalidaEsperada(),
                entity.getEsOculto()
        );
    }
}