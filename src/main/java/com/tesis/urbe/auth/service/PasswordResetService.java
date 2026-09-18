package com.tesis.urbe.auth.service;

import com.tesis.urbe.Email.Service.EmailService;
import com.tesis.urbe.auth.dto.ForgotPasswordDTO;
import com.tesis.urbe.auth.dto.ResetPasswordDTO;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // Almacena temporalmente: Key = Correo, Value = ResetCodeDetails
    private final Map<String, ResetCodeDetails> tokenStorage = new ConcurrentHashMap<>();

    public PasswordResetService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void generateAndSendCode(ForgotPasswordDTO request) {
        UserEntity usuario = userRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("El correo proporcionado no se encuentra registrado"));

        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

        tokenStorage.put(usuario.getCorreo(), new ResetCodeDetails(code, expiration));

        emailService.sendResetPasswordCode(usuario.getCorreo(), code);
    }

    public void resetPassword(ResetPasswordDTO request) {
        ResetCodeDetails details = tokenStorage.get(request.correo());

        if (details == null) {
            throw new RuntimeException("No se ha solicitado una recuperación de contraseña para este correo");
        }

        if (LocalDateTime.now().isAfter(details.expiration())) {
            tokenStorage.remove(request.correo());
            throw new RuntimeException("El código ha expirado. Por favor solicita uno nuevo");
        }

        if (!details.code().equals(request.codigo())) {
            throw new RuntimeException("El código de verificación es incorrecto");
        }

        UserEntity usuario = userRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setContrasena(passwordEncoder.encode(request.nuevaContrasena()));
        userRepository.save(usuario);

        // Invalida el código tras ser consumido
        tokenStorage.remove(request.correo());
    }

    private record ResetCodeDetails(String code, LocalDateTime expiration) {}
}