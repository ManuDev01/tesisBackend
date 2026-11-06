package com.tesis.urbe.user.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tesis.urbe.rol.entity.RolEntity;
import com.tesis.urbe.user.dto.DeleteUserDTO;
import com.tesis.urbe.user.dto.UpdateUserDTO;
import com.tesis.urbe.user.dto.UserRankingDTO;
import com.tesis.urbe.user.entity.UserEntity;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tesis.urbe.user.dto.UserDTO;
import com.tesis.urbe.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public long countUsers() {
        return userRepository.count();
    }

    // GET - Obtener todos los usuarios
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer idUsuario) {
        return userRepository.findById(idUsuario)
                .map(UserDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + idUsuario));
    }

    // POST - Guardar usuario directo
    public UserDTO saveUser(UserDTO userDTO) {

        String passwordEncriptada = this.passwordEncoder.encode(userDTO.contrasena());

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
                passwordEncriptada, // Aquí pasa la contraseña limpia del frontend
                rol,
                userDTO.activo()
        );

        UserEntity savedEntity = userRepository.save(userEntity);
        return UserDTO.fromEntity(savedEntity);
    }

    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO) {
    UserEntity usuario = userRepository.findById(updateUserDTO.idUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + updateUserDTO.idUsuario()));

    usuario.setPrimerNombre(updateUserDTO.primerNombre());
    usuario.setSegundoNombre(updateUserDTO.segundoNombre());
    usuario.setPrimerApellido(updateUserDTO.primerApellido());
    usuario.setSegundoApellido(updateUserDTO.segundoApellido());
    usuario.setNombreUsuario(updateUserDTO.nombreUsuario());
    usuario.setCedula(updateUserDTO.cedula());
    usuario.setCorreo(updateUserDTO.correo());

    UserEntity usuarioActualizado = userRepository.save(usuario);

    return UpdateUserDTO.fromEntity(usuarioActualizado);
}

    public DeleteUserDTO deleteUser(DeleteUserDTO deleteUserDTO) {
        UserEntity usuario = userRepository.findById(deleteUserDTO.idUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(false);

        UserEntity usuarioEliminado = userRepository.save(usuario);

        return DeleteUserDTO.fromEntity(usuarioEliminado);
    }

    public Integer obtainExp(Integer idUsuario) {
        // Experiencia total (lecciones + proyectos + medallas) calculada en UNA
        // sola consulta; antes eran 6 consultas sueltas por usuario.
        return userRepository.calcularExpDeUsuario(idUsuario)
                .map(Long::intValue)
                .orElse(0);
    }

    public List<UserRankingDTO> getUsersOrderByExp() {
        // UNA sola consulta para todo el ranking (antes: findAll + obtainExp por
        // cada usuario + carga perezosa del rol al filtrar administradores).
        return userRepository.findRankingExpEstudiantes().stream()
                .map(fila -> new UserRankingDTO(
                        (Integer) fila[0],
                        (String) fila[1],
                        (String) fila[2],
                        (String) fila[3],
                        ((Number) fila[4]).intValue()
                ))
                .sorted((u1, u2) -> u2.totalExp().compareTo(u1.totalExp())) // Orden descendente
                .collect(Collectors.toList());
    }
}
