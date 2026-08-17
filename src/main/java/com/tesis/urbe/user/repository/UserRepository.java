package com.tesis.urbe.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tesis.urbe.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Busca coincidencia por nombreUsuario O por correo
    Optional<UserEntity> findByNombreUsuarioOrCorreo(String nombreUsuario, String correo);

}