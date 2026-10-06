package com.tesis.urbe.casosDeUso.service;

import com.tesis.urbe.casosDeUso.dto.CasoDeUsoProyectoCursoDTO;
import com.tesis.urbe.casosDeUso.dto.CasosDeUsoDTO;
import com.tesis.urbe.casosDeUso.repository.CasoDeUsoProyectoCursoRepository;
import com.tesis.urbe.casosDeUso.repository.CasosDeUsoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasosDeUsoService {

    private final CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository;
    private final CasosDeUsoRepository casosDeUsoRepository;

    public CasosDeUsoService(
            CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository,
            CasosDeUsoRepository casosDeUsoRepository
    ) {
        this.casoDeUsoProyectoCursoRepository = casoDeUsoProyectoCursoRepository;
        this.casosDeUsoRepository = casosDeUsoRepository;
    }

    public List<CasoDeUsoProyectoCursoDTO> obtenerCasosVisiblesProyectoCurso(Integer idProyectoCurso) {
        return casoDeUsoProyectoCursoRepository
                .findByIdProyectoCursoAndEsOcultoFalse(idProyectoCurso)
                .stream()
                .map(CasoDeUsoProyectoCursoDTO::fromEntity)
                .toList();
    }

    public List<CasosDeUsoDTO> obtenerCasosVisiblesProyecto(Integer idProyecto) {
        return casosDeUsoRepository
                .findByIdProyecto_IdProyecto(idProyecto)
                .stream()
                .map(CasosDeUsoDTO::fromEntity)
                .toList();
    }
}