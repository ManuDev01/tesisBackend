package com.tesis.urbe.medallas.service;

import com.tesis.urbe.medallas.LogroMedalla;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.dto.UsuarioMedallaCountDTO;
import com.tesis.urbe.medallas.entity.MedallaEntity;
import com.tesis.urbe.medallas.entity.UsuarioConMedallaEntity;
import com.tesis.urbe.medallas.repository.MedallaRepository;
import com.tesis.urbe.medallas.repository.UsuarioConMedallaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Set<Integer> medallasObtenidasIds = obtenerIdsMedallas(idUsuario);

        return medallaRepository.findAll()
                .stream()
                .map(medalla -> {
                    boolean tieneMedalla = medallasObtenidasIds.contains(medalla.getIdMedalla());
                    return MedallaDTO.fromEntity(medalla, tieneMedalla);
                })
                .collect(Collectors.toList());
    }

    /**
     * Desbloquea una medalla para un usuario de forma idempotente.
     *
     * @return la medalla recién desbloqueada, o {@code null} si ya la tenía,
     *         no existe o los datos son inválidos.
     */
    @Transactional
    public MedallaDTO desbloquear(Integer idUsuario, Integer idMedalla) {
        if (idUsuario == null || idMedalla == null) {
            return null;
        }

        if (usuarioConMedallaRepository.existsByIdUsuarioAndIdMedalla(idUsuario, idMedalla)) {
            return null;
        }

        MedallaEntity medalla = medallaRepository.findById(idMedalla).orElse(null);
        if (medalla == null) {
            return null;
        }

        usuarioConMedallaRepository.save(new UsuarioConMedallaEntity(idUsuario, idMedalla));

        return MedallaDTO.fromEntity(medalla, true);
    }

    @Transactional
    public MedallaDTO desbloquear(Integer idUsuario, LogroMedalla logro) {
        return desbloquear(idUsuario, logro != null ? logro.getId() : null);
    }

    public boolean tieneMedalla(Integer idUsuario, LogroMedalla logro) {
        if (idUsuario == null || logro == null) {
            return false;
        }
        return usuarioConMedallaRepository.existsByIdUsuarioAndIdMedalla(idUsuario, logro.getId());
    }

    public Set<Integer> obtenerIdsMedallas(Integer idUsuario) {
        if (idUsuario == null) {
            return Set.of();
        }
        return usuarioConMedallaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConMedallaEntity::getIdMedalla)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void guardarUsuarioConMedalla(Integer idUsuario, Integer idMedalla) {
        desbloquear(idUsuario, idMedalla);
    }

    public List<UsuarioMedallaCountDTO> getRankingUsuariosConMasMedallas() {
        return usuarioConMedallaRepository.obtenerRankingUsuariosConMasMedallas();
    }
}
