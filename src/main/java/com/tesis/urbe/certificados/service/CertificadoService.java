package com.tesis.urbe.certificados.service;

import com.tesis.urbe.certificados.dto.CertificadoResponseDTO;
import com.tesis.urbe.certificados.dto.GuardarCertificadoDTO;
import com.tesis.urbe.certificados.entity.CertificadosEntity;
import com.tesis.urbe.certificados.repository.CertificadosRepository;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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

        CertificadosEntity entity = new CertificadosEntity(
                dto.idUsuario(),
                dto.idCurso(),
                dto.titulo(),
                dto.descripcion()
        );

        CertificadosEntity guardado = certificadosRepository.save(entity);

        return new CertificadoResponseDTO(
                guardado.getIdCertificado(),
                usuario.getIdUsuario(),
                usuario.getCedula(),
                guardado.getIdCurso(),
                guardado.getTitulo(),
                guardado.getDescripcion(),
                guardado.getFechaEmision()
        );
    }

    public List<CertificadoResponseDTO> getCertificadosByUsuario(Integer idUsuario) {
        UserEntity usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        return certificadosRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(cert -> new CertificadoResponseDTO(
                        cert.getIdCertificado(),
                        usuario.getIdUsuario(),
                        usuario.getCedula(),
                        cert.getIdCurso(),
                        cert.getTitulo(),
                        cert.getDescripcion(),
                        cert.getFechaEmision()
                ))
                .collect(Collectors.toList());
    }
}