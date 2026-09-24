package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UsuarioConProyectoRepository extends ListCrudRepository<UsuarioConProyectosEntity, Integer> {

    // Este es el método que te faltaba definir:
    List<UsuarioConProyectosEntity> findByIdUsuario(Integer idUsuario);
}