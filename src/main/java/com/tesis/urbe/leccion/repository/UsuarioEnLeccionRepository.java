package com.tesis.urbe.leccion.repository;

import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioEnLeccionRepository extends JpaRepository<UsuarioEnLeccionEntity, Integer> {

    // Método para verificar si el usuario ya completó la lección antes de insertar
    boolean existsByIdLeccionAndIdUsuario(Integer idLeccion, Integer idUsuario);

    // Método para consultar todas las lecciones completadas de un usuario
    List<UsuarioEnLeccionEntity> findByIdUsuario(Integer idUsuario);
}