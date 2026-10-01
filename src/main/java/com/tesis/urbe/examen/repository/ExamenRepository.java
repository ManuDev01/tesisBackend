package com.tesis.urbe.examen.repository;

import com.tesis.urbe.examen.entity.ExamenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamenRepository extends JpaRepository<ExamenEntity, Integer> {
    Optional<ExamenEntity> findBySeccion_IdSeccion(Integer idSeccion);
}