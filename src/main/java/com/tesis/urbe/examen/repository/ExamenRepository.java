package com.tesis.urbe.examen.repository;

import com.tesis.urbe.examen.entity.ExamenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
    public interface ExamenRepository extends JpaRepository<ExamenEntity, Integer> {

        // Solo se necesita la sección (id/título/descripción/puntos); cargar
        // "preguntas" aquí traía todas las preguntas de cada examen sin usarlas.
        @EntityGraph(attributePaths = {"seccion"})
        Optional<ExamenEntity> findBySeccion_IdSeccion(Integer idSeccion);

        // Examen con sus preguntas en UNA sola consulta (antes: findById +
        // preguntas perezosas en una segunda consulta dependiente de Open-View).
        // Derived query + EntityGraph, igual que findBySeccion_IdSeccion.
        @EntityGraph(attributePaths = {"preguntas"})
        Optional<ExamenEntity> findByIdExamen(Integer idExamen);
    }
