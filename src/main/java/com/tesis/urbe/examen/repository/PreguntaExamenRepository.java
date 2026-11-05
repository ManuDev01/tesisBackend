package com.tesis.urbe.examen.repository;

import com.tesis.urbe.examen.entity.PreguntaExamenEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface PreguntaExamenRepository extends ListCrudRepository<PreguntaExamenEntity, Integer> {

        // Cambiado de findByIdExamen a findByExamen_IdExamen para que coincida con la relación
        List<PreguntaExamenEntity> findByExamen_IdExamen(Integer idExamen);
    }
