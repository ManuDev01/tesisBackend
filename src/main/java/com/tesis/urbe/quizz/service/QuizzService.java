package com.tesis.urbe.quizz.service;

import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.repository.QuizzRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizzService {

    private final QuizzRepository quizzRepository;

    public QuizzService(QuizzRepository quizzRepository) {
        this.quizzRepository = quizzRepository;
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



}
