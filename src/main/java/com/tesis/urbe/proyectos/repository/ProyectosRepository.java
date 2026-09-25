package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProyectosRepository extends ListCrudRepository<ProyectosEntity, Integer> {
    List<ProyectosEntity> findByIdSeccion_IdSeccion(Integer idSeccion);
    List<ProyectosEntity> findByIdProyectoIn(List<Integer> idsProyectos);
}
