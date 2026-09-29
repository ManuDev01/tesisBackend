package com.tesis.urbe.certificados.dto;

public record GuardarCertificadoDTO(
        Integer idUsuario,
        Integer idCurso,
        String titulo,
        String descripcion
) {}