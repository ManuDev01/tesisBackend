package com.tesis.urbe.casosDeUso.repository;

import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasosDeUsoRepository extends JpaRepository<CasosDeUsoEntity, Integer> {
    // Retorna la lista de todos los casos de uso del proyecto
    List<CasosDeUsoEntity> findByIdProyecto_IdProyecto(Integer idProyecto);
}