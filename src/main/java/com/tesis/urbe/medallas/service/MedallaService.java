package com.tesis.urbe.medallas.service;

import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.entity.UsuarioConMedallaEntity;
import com.tesis.urbe.medallas.repository.MedallaRepository;
import com.tesis.urbe.medallas.repository.UsuarioConMedallaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedallaService {

    private final MedallaRepository medallaRepository;
    private final UsuarioConMedallaRepository usuarioConMedallaRepository;

    public MedallaService(MedallaRepository medallaRepository, UsuarioConMedallaRepository usuarioConMedallaRepository) {
        this.medallaRepository = medallaRepository;
        this.usuarioConMedallaRepository = usuarioConMedallaRepository;
    }

    public List<MedallaDTO> getMedallasByUsuario(Integer idUsuario) {
        Set<Integer> medallasObtenidasIds = usuarioConMedallaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConMedallaEntity::getIdMedalla)
                .collect(Collectors.toSet());

        return medallaRepository.findAll()
                .stream()
                .map(medalla -> {
                    boolean tieneMedalla = medallasObtenidasIds.contains(medalla.getIdMedalla());
                    return MedallaDTO.fromEntity(medalla, tieneMedalla);
                })
                .collect(Collectors.toList());
    }

    public void guardarUsuarioConMedalla(Integer idUsuario, Integer idMedalla) {
        UsuarioConMedallaEntity entity = new UsuarioConMedallaEntity(idUsuario, idMedalla);
        usuarioConMedallaRepository.save(entity);
    }
}