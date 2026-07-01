package com.tesis.urbe.user.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tesis.urbe.rol.entity.RolEntity;
import com.tesis.urbe.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesis.urbe.user.dto.UserDTO;
import com.tesis.urbe.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // GET - Obtener todos los usuarios
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // POST - Guardar usuario directo
    public UserDTO saveUser(UserDTO userDTO) {
        // Armamos el objeto de Rol si viene el ID
        RolEntity rol = null;
        if (userDTO.idRol() != null) {
            rol = new RolEntity();
            rol.setIdRol(userDTO.idRol());
        }

        // Usamos el constructor completo de tu UserEntity (idUsuario va null porque es IDENTITY)
        UserEntity userEntity = new UserEntity(
                null,
                userDTO.primerNombre(),
                userDTO.segundoNombre(),
                userDTO.primerApellido(),
                userDTO.segundoApellido(),
                userDTO.nombreUsuario(),
                userDTO.cedula(),
                userDTO.correo(),
                userDTO.contrasena(), // Aquí pasa la contraseña limpia del frontend
                rol,
                userDTO.activo()
        );

        UserEntity savedEntity = userRepository.save(userEntity);
        return UserDTO.fromEntity(savedEntity);
    }
}