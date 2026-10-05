package com.tesis.urbe.certificados.service;

import com.tesis.urbe.certificados.dto.CertificadoResponseDTO;
import com.tesis.urbe.certificados.dto.GuardarCertificadoDTO;
import com.tesis.urbe.certificados.entity.CertificadosEntity;
import com.tesis.urbe.certificados.entity.UsuarioCertificadoEntity;
import com.tesis.urbe.certificados.repository.CertificadosRepository;
import com.tesis.urbe.certificados.repository.UsuarioCertificadoRepository;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificadoService {

    private final CertificadosRepository certificadosRepository;
    private final UsuarioCertificadoRepository usuarioCertificadoRepository;
    private final UserRepository userRepository;

    public CertificadoService(
            CertificadosRepository certificadosRepository,
            UsuarioCertificadoRepository usuarioCertificadoRepository,
            UserRepository userRepository) {
        this.certificadosRepository = certificadosRepository;
        this.usuarioCertificadoRepository = usuarioCertificadoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CertificadoResponseDTO guardarCertificado(GuardarCertificadoDTO dto) {
        UserEntity usuario = userRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.idUsuario()));

        CertificadosEntity certificado = certificadosRepository.findByIdCurso(dto.idCurso())
                .orElseThrow(() -> new RuntimeException("No existe plantilla de certificado para el curso ID: " + dto.idCurso()));

        if (usuarioCertificadoRepository.existsByIdUsuarioAndIdCertificado(usuario.getIdUsuario(), certificado.getIdCertificado())) {
            throw new RuntimeException("El usuario ya tiene un certificado emitido para este curso.");
        }

        String uidCertificado = generarUidCertificadoSHA256(usuario.getIdUsuario(), certificado.getIdCurso(), certificado.getTitulo());

        UsuarioCertificadoEntity relacion = new UsuarioCertificadoEntity(
                usuario.getIdUsuario(),
                certificado.getIdCertificado(),
                uidCertificado
        );

        UsuarioCertificadoEntity guardado = usuarioCertificadoRepository.save(relacion);

        return new CertificadoResponseDTO(
                certificado.getIdCertificado(),
                usuario.getIdUsuario(),
                construirNombreCompleto(usuario),
                usuario.getCedula(),
                certificado.getIdCurso(),
                certificado.getTitulo(),
                certificado.getDescripcion(),
                guardado.getFechaEmision(),
                guardado.getUidCertificado()
        );
    }

    public List<CertificadoResponseDTO> getCertificadosByIdUsuario(Integer idUsuario) {
        UserEntity usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        String nombreCompleto = construirNombreCompleto(usuario);

        return usuarioCertificadoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(rel -> {
                    CertificadosEntity cert = certificadosRepository.findById(rel.getIdCertificado())
                            .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));
                    return new CertificadoResponseDTO(
                            cert.getIdCertificado(),
                            usuario.getIdUsuario(),
                            nombreCompleto,
                            usuario.getCedula(),
                            cert.getIdCurso(),
                            cert.getTitulo(),
                            cert.getDescripcion(),
                            rel.getFechaEmision(),
                            rel.getUidCertificado()
                    );
                })
                .collect(Collectors.toList());
    }

    public CertificadoResponseDTO getCertificadoByUid(String uidCertificado) {
        UsuarioCertificadoEntity rel = usuarioCertificadoRepository.findByUidCertificado(uidCertificado)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado con UID: " + uidCertificado));

        UserEntity usuario = userRepository.findById(rel.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + rel.getIdUsuario()));

        CertificadosEntity cert = certificadosRepository.findById(rel.getIdCertificado())
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));

        return new CertificadoResponseDTO(
                cert.getIdCertificado(),
                usuario.getIdUsuario(),
                construirNombreCompleto(usuario),
                usuario.getCedula(),
                cert.getIdCurso(),
                cert.getTitulo(),
                cert.getDescripcion(),
                rel.getFechaEmision(),
                rel.getUidCertificado()
        );
    }

    public boolean existeCertificado(Integer idUsuario, Integer idCurso) {
        return certificadosRepository.findByIdCurso(idCurso)
                .map(cert -> usuarioCertificadoRepository.existsByIdUsuarioAndIdCertificado(idUsuario, cert.getIdCertificado()))
                .orElse(false);
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
            throw new RuntimeException("Error al generar UID del certificado", e);
        }
    }
}