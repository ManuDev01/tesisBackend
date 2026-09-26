package com.tesis.urbe.user.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.leccion.repository.UsuarioEnLeccionRepository;
import com.tesis.urbe.medallas.entity.UsuarioConMedallaEntity;
import com.tesis.urbe.medallas.repository.MedallaRepository;
import com.tesis.urbe.medallas.repository.UsuarioConMedallaRepository;
import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import com.tesis.urbe.proyectos.repository.ProyectosRepository;
import com.tesis.urbe.proyectos.repository.UsuarioConProyectoRepository;
import com.tesis.urbe.rol.entity.RolEntity;
import com.tesis.urbe.user.dto.DeleteUserDTO;
import com.tesis.urbe.user.dto.UpdateUserDTO;
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
    private final UsuarioEnLeccionRepository usuarioEnLeccionRepository;
    private final LeccionRepository leccionRepository;
    private final UsuarioConProyectoRepository usuarioConProyectoRepository;
    private final ProyectosRepository proyectosRepository;
    private final UsuarioConMedallaRepository usuarioConMedallaRepository;
    private final MedallaRepository medallaRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UsuarioEnLeccionRepository usuarioEnLeccionRepository, LeccionRepository leccionRepository, UsuarioConProyectoRepository usuarioConProyectoRepository, ProyectosRepository proyectosRepository, UsuarioConMedallaRepository usuarioConMedallaRepository, MedallaRepository medallaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioEnLeccionRepository = usuarioEnLeccionRepository;
        this.leccionRepository = leccionRepository;
        this.usuarioConProyectoRepository = usuarioConProyectoRepository;
        this.proyectosRepository = proyectosRepository;
        this.usuarioConMedallaRepository = usuarioConMedallaRepository;
        this.medallaRepository = medallaRepository;
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

    public void getUsersOrderByExp() {
        
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
        UserEntity usuario = userRepository.findById(updateUserDTO.idUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
        // 1. Puntos acumulados por Lecciones
        List<Integer> idsLecciones = usuarioEnLeccionRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioEnLeccionEntity::getIdLeccion)
                .toList();

        int expLecciones = idsLecciones.isEmpty() ? 0 : leccionRepository.findByIdLeccionIn(idsLecciones)
                .stream()
                .mapToInt(leccion -> leccion.getPuntosLeccion() != null ? leccion.getPuntosLeccion() : 0)
                .sum();

        // 2. Puntos acumulados por Proyectos
        List<Integer> idsProyectos = usuarioConProyectoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConProyectosEntity::getIdProyecto)
                .toList();

        int expProyectos = idsProyectos.isEmpty() ? 0 : proyectosRepository.findByIdProyectoIn(idsProyectos)
                .stream()
                .mapToInt(proyecto -> proyecto.getPuntosProyectos() != null ? proyecto.getPuntosProyectos() : 0)
                .sum();

        // 3. Puntos acumulados por Medallas
        List<Integer> idsMedallas = usuarioConMedallaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConMedallaEntity::getIdMedalla)
                .toList();

        int expMedallas = idsMedallas.isEmpty() ? 0 : medallaRepository.findByIdMedallaIn(idsMedallas)
                .stream()
                .mapToInt(medalla -> medalla.getValorMedalla() != null ? medalla.getValorMedalla() : 0)
                .sum();

        // Suma total de experiencia
        return expLecciones + expProyectos + expMedallas;
    }

    public List<UserRankingDTO> getUsersOrderByExp() {
    return userRepository.findAll()
            .stream()
            .map(user -> {
                Integer exp = obtainExp(user.getIdUsuario());
                return new UserRankingDTO(
                        user.getIdUsuario(),
                        user.getNombreUsuario(),
                        user.getPrimerNombre(),
                        user.getPrimerApellido(),
                        exp
                );
            })
            .sorted((u1, u2) -> u2.totalExp().compareTo(u1.totalExp())) // Orden descendente
            .collect(Collectors.toList());
}
}