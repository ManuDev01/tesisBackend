package com.tesis.urbe.auth.controller;

import com.tesis.urbe.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesis.urbe.auth.dto.LoginRequestDTO;
import com.tesis.urbe.auth.dto.LoginResponseDTO;
import com.tesis.urbe.user.service.UserService;

@RestController
@RequestMapping("/auth") // Ahora la URL será http://localhost:8080/auth/login
public class AuthController {

    private final AuthService authService;

    // TODO: Agregar para iniciar sesion tanto con correo como con nombre de usuario

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}