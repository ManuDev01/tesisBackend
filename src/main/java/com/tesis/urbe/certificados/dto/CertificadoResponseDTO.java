package com.tesis.urbe.certificados.dto;

import java.time.LocalDateTime;

public record CertificadoResponseDTO(
        Integer idCertificado,
        Integer studentId,
        String studentDni,
        Integer courseId,
        String titulo,
        String descripcion,
        LocalDateTime issueDate
) {}