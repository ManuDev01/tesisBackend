package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.EvaluacionProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionProyectoRepository extends JpaRepository<EvaluacionProyectoEntity, Integer> {

    List<EvaluacionProyectoEntity> findByIdUsuarioAndIdProyecto(Integer idUsuario, Integer idProyecto);

    Optional<EvaluacionProyectoEntity> findTopByIdUsuarioAndIdProyectoOrderByIdEvaluacionProyectoDesc(Integer idUsuario, Integer idProyecto);
}
