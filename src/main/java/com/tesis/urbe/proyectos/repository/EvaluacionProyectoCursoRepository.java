package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.EvaluacionProyectoCursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionProyectoCursoRepository extends JpaRepository<EvaluacionProyectoCursoEntity, Integer> {

    List<EvaluacionProyectoCursoEntity> findByIdUsuarioAndIdProyectoCurso(Integer idUsuario, Integer idProyectoCurso);

    Optional<EvaluacionProyectoCursoEntity> findTopByIdUsuarioAndIdProyectoCursoOrderByIdEvaluacionProyectoCursoDesc(Integer idUsuario, Integer idProyectoCurso);
}
