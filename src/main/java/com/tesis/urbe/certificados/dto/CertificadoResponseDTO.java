package com.tesis.urbe.certificados.dto;

import java.time.LocalDateTime;

public record CertificadoResponseDTO(
        Integer idCertificado,
        Integer idUsuario,
        String nombreCompleto, // Se añade para el nombre concatenado
        String cedula,
        Integer idCurso,
        String titulo,
        String descripcion,
        LocalDateTime fechaEmision,
        String uidCertificado
) {}