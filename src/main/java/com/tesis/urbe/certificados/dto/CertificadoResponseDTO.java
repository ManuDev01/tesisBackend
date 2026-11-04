package com.tesis.urbe.certificados.dto;

import com.tesis.urbe.medallas.dto.MedallaDTO;

import java.time.LocalDateTime;
import java.util.List;

public record CertificadoResponseDTO(
        Integer idCertificado,
        Integer idUsuario,
        String nombreCompleto, // Se añade para el nombre concatenado
        String cedula,
        Integer idCurso,
        String titulo,
        String descripcion,
        LocalDateTime fechaEmision,
        String uidCertificado,
        List<MedallaDTO> medallas // Medallas desbloqueadas al emitir este certificado
) {

    /** Solo lectura: no hay medallas nuevas que informar. */
    public CertificadoResponseDTO(Integer idCertificado,
                                  Integer idUsuario,
                                  String nombreCompleto,
                                  String cedula,
                                  Integer idCurso,
                                  String titulo,
                                  String descripcion,
                                  LocalDateTime fechaEmision,
                                  String uidCertificado) {
        this(idCertificado, idUsuario, nombreCompleto, cedula, idCurso, titulo,
                descripcion, fechaEmision, uidCertificado, List.of());
    }
}
