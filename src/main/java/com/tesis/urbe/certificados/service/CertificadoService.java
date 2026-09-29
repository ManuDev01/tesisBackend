package com.tesis.urbe.certificados.service;

import com.tesis.urbe.certificados.dto.CertificadoResponseDTO;
import com.tesis.urbe.certificados.dto.GuardarCertificadoDTO;
import com.tesis.urbe.certificados.entity.CertificadosEntity;
import com.tesis.urbe.certificados.repository.CertificadosRepository;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificadoService {

    private final CertificadosRepository certificadosRepository;
    private final UserRepository userRepository;

    public CertificadoService(CertificadosRepository certificadosRepository, UserRepository userRepository) {
        this.certificadosRepository = certificadosRepository;
        this.userRepository = userRepository;
    }

    public CertificadoResponseDTO guardarCertificado(GuardarCertificadoDTO dto) {
        UserEntity usuario = userRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.idUsuario()));

        String uidCertificado = generarUidCertificadoSHA256(dto.idUsuario(), dto.idCurso(), dto.titulo());

        CertificadosEntity entity = new CertificadosEntity(
                dto.idUsuario(),
                dto.idCurso(),
                dto.titulo(),
                dto.descripcion(),
                uidCertificado
        );

        CertificadosEntity guardado = certificadosRepository.save(entity);

        return new CertificadoResponseDTO(
                guardado.getIdCertificado(),
                usuario.getIdUsuario(),
                construirNombreCompleto(usuario),
                usuario.getCedula(),
                guardado.getIdCurso(),
                guardado.getTitulo(),
                guardado.getDescripcion(),
                guardado.getFechaEmision(),
                guardado.getUidCertificado()
        );
    }

    public List<CertificadoResponseDTO> getCertificadosByIdUsuario(Integer idUsuario) {
        UserEntity usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        String nombreCompleto = construirNombreCompleto(usuario);

        return certificadosRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(certificado -> new CertificadoResponseDTO(
                        certificado.getIdCertificado(),
                        usuario.getIdUsuario(),
                        nombreCompleto,
                        usuario.getCedula(),
                        certificado.getIdCurso(),
                        certificado.getTitulo(),
                        certificado.getDescripcion(),
                        certificado.getFechaEmision(),
                        certificado.getUidCertificado()
                ))
                .collect(Collectors.toList());
    }

    public CertificadoResponseDTO getCertificadoByUid(String uidCertificado) {
        CertificadosEntity certificado = certificadosRepository.findByUidCertificado(uidCertificado)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado con UID: " + uidCertificado));

        UserEntity usuario = userRepository.findById(certificado.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + certificado.getIdUsuario()));

        return new CertificadoResponseDTO(
                certificado.getIdCertificado(),
                usuario.getIdUsuario(),
                construirNombreCompleto(usuario),
                usuario.getCedula(),
                certificado.getIdCurso(),
                certificado.getTitulo(),
                certificado.getDescripcion(),
                certificado.getFechaEmision(),
                certificado.getUidCertificado()
        );
    }

    private String construirNombreCompleto(UserEntity usuario) {
        return String.format("%s %s",
                usuario.getPrimerNombre() != null ? usuario.getPrimerNombre() : "",
                usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : ""
        ).replaceAll("\\s+", " ").trim();
    }

    private String generarUidCertificadoSHA256(Integer idUsuario, Integer idCurso, String titulo) {
        try {
            String rawData = idUsuario + "-" + idCurso + "-" + titulo + "-" + System.nanoTime();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            String hex = hexString.toString();

            return String.format("UC-%s-%s-%s-%s-%s",
                    hex.substring(0, 8),
                    hex.substring(8, 12),
                    hex.substring(12, 16),
                    hex.substring(16, 20),
                    hex.substring(20, 32)
            );

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el algoritmo SHA-256 para el certificado", e);
        }
    }
}