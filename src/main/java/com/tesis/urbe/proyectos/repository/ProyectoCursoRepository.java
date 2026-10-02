package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.ProyectoCursoEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProyectoCursoRepository extends ListCrudRepository<ProyectoCursoEntity, Integer> {

    Optional<ProyectoCursoEntity> findByIdCurso(Integer idCurso);
}