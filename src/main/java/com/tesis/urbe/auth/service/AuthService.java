package com.tesis.urbe.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tesis.urbe.auth.dto.LoginRequestDTO;
import com.tesis.urbe.auth.dto.LoginResponseDTO;
import com.tesis.urbe.user.dto.UserDTO;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import com.tesis.urbe.user.service.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 1. Buscar coincidencia por nombreUsuario O por correo
        // (loginRequest.nombreUsuario() recibe tanto el username como el email enviado desde el frontend)
        String identificador = loginRequest.nombreUsuario();

        UserEntity user = userRepository.findByNombreUsuarioOrCorreo(identificador, identificador)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // 2. Verificar estado activo
        if (!user.isActivo()) {
            throw new RuntimeException("El usuario está inactivo");
        }

        // 3. Validar contraseña con PasswordEncoder
        if (!passwordEncoder.matches(loginRequest.contrasena(), user.getContrasena())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 4. Generar token
        String token = jwtService.generateToken(user);

        // 5. Retornar respuesta
        return new LoginResponseDTO(token, UserDTO.fromEntity(user));
    }
}