package com.tesis.urbe.quizz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tesis.urbe.quizz.dto.FalloUsuarioQuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.entity.FalloUsuarioQuizzEntity;
import com.tesis.urbe.quizz.entity.QuizzEntity;
import com.tesis.urbe.quizz.repository.FalloUsuarioQuizzRepository;
import com.tesis.urbe.quizz.repository.QuizzRepository;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;

@Service
public class QuizzService {

    private final QuizzRepository quizzRepository;
    private final FalloUsuarioQuizzRepository falloUsuarioQuizzRepository;
    private final UserRepository userRepository;

    public QuizzService(QuizzRepository quizzRepository, 
                        FalloUsuarioQuizzRepository falloUsuarioQuizzRepository, 
                        UserRepository userRepository) {
        this.quizzRepository = quizzRepository;
        this.falloUsuarioQuizzRepository = falloUsuarioQuizzRepository;
        this.userRepository = userRepository;
    }

    public List<QuizzDTO> getQuizz() {
        return quizzRepository.findAll()
                .stream()
                .map(QuizzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<QuizzDTO> getQuizzByIdLeccion(Integer idLeccion) {
        return quizzRepository.findByIdLeccion_IdLeccion(idLeccion)
                .stream()
                .map(QuizzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Registrar fallo de quiz
    public FalloUsuarioQuizzDTO saveFalloUsuarioQuizz(FalloUsuarioQuizzDTO dto) {
        UserEntity usuario = userRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.idUsuario()));

        QuizzEntity quizz = quizzRepository.findById(dto.idQuizz())
                .orElseThrow(() -> new RuntimeException("Quizz no encontrado con ID: " + dto.idQuizz()));

        FalloUsuarioQuizzEntity entidad = new FalloUsuarioQuizzEntity(null, usuario, quizz);
        FalloUsuarioQuizzEntity guardado = falloUsuarioQuizzRepository.save(entidad);

        return FalloUsuarioQuizzDTO.fromEntity(guardado);
    }

    // Obtener fallos por usuario
    public List<FalloUsuarioQuizzDTO> getFalloUsuarioQuizzByIdUsuario(Integer idUsuario) {
        return falloUsuarioQuizzRepository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(FalloUsuarioQuizzDTO::fromEntity)
                .collect(Collectors.toList());
    }

// Obtener el quiz con más fallos para el dashboard
public QuizzFalloDashboardDTO getQuizzConMasFallos() {
    List<QuizzFalloDashboardDTO> resultado = falloUsuarioQuizzRepository.findQuizzConMasFallos(PageRequest.of(0, 1));
    return resultado.isEmpty() ? null : resultado.get(0);
}
}
