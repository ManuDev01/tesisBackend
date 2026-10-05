package com.tesis.urbe.casosDeUso.repository;


import com.tesis.urbe.casosDeUso.entity.CasoDeUsoProyectoCursoEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CasoDeUsoProyectoCursoRepository extends ListCrudRepository<CasoDeUsoProyectoCursoEntity, Integer> {
    List<CasoDeUsoProyectoCursoEntity> findByIdProyectoCurso(Integer idProyectoCurso);

    List<CasoDeUsoProyectoCursoEntity> findByIdProyectoCursoAndEsOcultoFalse(Integer idProyectoCurso);
}