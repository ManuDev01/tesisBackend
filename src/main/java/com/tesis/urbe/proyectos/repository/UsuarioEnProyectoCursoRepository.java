package com.tesis.urbe.proyectos.repository;

import com.tesis.urbe.proyectos.entity.UsuarioEnProyectoCursoEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioEnProyectoCursoRepository extends ListCrudRepository<UsuarioEnProyectoCursoEntity, Integer> {
    List<UsuarioEnProyectoCursoEntity> findByIdUsuario(Integer idUsuario);
    Optional<UsuarioEnProyectoCursoEntity> findByIdUsuarioAndIdProyectoCurso(Integer idUsuario, Integer idProyectoCurso);
}