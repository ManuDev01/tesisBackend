package com.tesis.urbe.curso.repository;

import com.tesis.urbe.curso.entity.CursoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CursoRepository extends ListCrudRepository<CursoEntity, Integer> {

    @EntityGraph(attributePaths = {"secciones", "secciones.lecciones", "secciones.examen", "secciones.proyectoCurso"})
    @Query("SELECT c FROM CursoEntity c WHERE c.idCurso = :idCurso")
    Optional<CursoEntity> findByIdWithFullGraph(@Param("idCurso") Integer idCurso);
}
