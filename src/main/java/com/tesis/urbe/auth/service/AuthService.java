//package com.tesis.urbe.auth.service;
//
//import org.springframework.stereotype.Service;
//
//import com.tesis.urbe.auth.dto.LoginRequestDTO;
//import com.tesis.urbe.auth.dto.LoginResponseDTO;
//import com.tesis.urbe.user.dto.UserDTO;
//import com.tesis.urbe.user.entity.UserEntity;
//import com.tesis.urbe.user.repository.UserRepository;
//import com.tesis.urbe.user.service.JwtService;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//
//    // Inyección por constructor
//    public AuthService(UserRepository userRepository, JwtService jwtService) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//    }
//
//    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
//        // 1. Buscar al usuario por su nombre de usuario en la BD
//        UserEntity user = userRepository.findAll().stream()
//                .filter(u -> u.getNombreUsuario().equals(loginRequest.nombreUsuario()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
//
//        // 2. Verificar que el usuario no esté baneado o inactivo
//        if (!user.isActivo()) {
//            throw new RuntimeException("El usuario está inactivo");
//        }
//
//        // 3. Validar la contraseña en texto plano (luego le metemos BCrypt si hace falta)
//        if (!user.getContrasena().equals(loginRequest.contrasena())) {
//            throw new RuntimeException("Credenciales incorrectas");
//        }
//
//        // 4. Delegar la generación del JWT al JwtService
//        String token = jwtService.generateToken(user);
//
//        // 5. Retornar el token junto a los datos del DTO limpios
//        return new LoginResponseDTO(token, UserDTO.fromEntity(user));
//    }
//}

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
    private final PasswordEncoder passwordEncoder; // Inyectamos el PasswordEncoder

    // Inyección por constructor
    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 1. Buscar al usuario por nombre de usuario en la BD (Recomendado: definir findByNombreUsuario en el repository)
        UserEntity user = userRepository.findByNombreUsuario(loginRequest.nombreUsuario())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // 2. Verificar que el usuario no esté inactivo
        if (!user.isActivo()) {
            throw new RuntimeException("El usuario está inactivo");
        }

        // 3. Validar la contraseña encriptada con BCrypt / PasswordEncoder
        // passwordEncoder.matches(textoPlano, hashDeBaseDeDatos)
        if (!passwordEncoder.matches(loginRequest.contrasena(), user.getContrasena())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 4. Delegar la generación del JWT al JwtService
        String token = jwtService.generateToken(user);

        // 5. Retornar el token junto a los datos del DTO limpios
        return new LoginResponseDTO(token, UserDTO.fromEntity(user));
    }
}