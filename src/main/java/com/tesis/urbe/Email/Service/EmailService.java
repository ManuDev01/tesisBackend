package com.tesis.urbe.Email.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {




    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public void sendResetPasswordCode(String toEmail, String code) {
        Map<String, Object> body = Map.of(
                "from", "Soporte <onboarding@resend.dev>", // Usar onboarding@resend.dev en pruebas antes de verificar tu dominio
                "to", List.of(toEmail),
                "subject", "Código de Recuperación de Contraseña",
                "html", "<h3>Tu código de verificación es:</h3>" +
                        "<h1 style='color: #4A90E2; letter-spacing: 2px;'>" + code + "</h1>" +
                        "<p>Este código vencerá en 15 minutos.</p>"
        );

        restClient.post()
                .uri("https://api.resend.com/emails")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}