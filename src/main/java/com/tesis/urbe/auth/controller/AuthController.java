package com.tesis.urbe.auth.controller;

import com.tesis.urbe.auth.dto.ForgotPasswordDTO;
import com.tesis.urbe.auth.dto.ResetPasswordDTO;
import com.tesis.urbe.auth.service.AuthService;
import com.tesis.urbe.auth.service.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

    // TODO: Agregar para iniciar sesion tanto con correo como con nombre de usuario

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
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

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        try {
            passwordResetService.generateAndSendCode(request);
            return ResponseEntity.ok("Código de verificación enviado exitosamente al correo");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok("Contraseña restablecida exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}