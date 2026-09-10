package com.tesis.urbe.leccion.service;

import com.tesis.urbe.leccion.dto.LeccionDTO;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeccionService {

    private final LeccionRepository leccionRepository;

    public LeccionService(LeccionRepository leccionRepository) {
        this.leccionRepository = leccionRepository;
    }

    public List<LeccionDTO> getAllLeccions() {
        return leccionRepository.findAll()
                .stream()
                .map(LeccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LeccionDTO> getQuizzByIdSeccion (Integer idSeccion) {
        return leccionRepository.findByIdSeccion_IdSeccion(idSeccion)
                .stream()
                .map(LeccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void completarLeccion (Integer idSeccion) {
        return;
    }
}
