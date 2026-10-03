package com.tesis.urbe.examen.service;

import com.tesis.urbe.examen.dto.ExamenDTO;
import com.tesis.urbe.examen.dto.PreguntaExamenDTO;
import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity;
import com.tesis.urbe.examen.repository.ExamenRepository;
import com.tesis.urbe.examen.repository.PreguntaExamenRepository;
import com.tesis.urbe.examen.repository.UsuarioEnExamenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final PreguntaExamenRepository preguntaExamenRepository;
    private final UsuarioEnExamenRepository usuarioEnExamenRepository;

    public ExamenService(ExamenRepository examenRepository, PreguntaExamenRepository preguntaExamenRepository, UsuarioEnExamenRepository usuarioEnExamenRepository) {
        this.examenRepository = examenRepository;
        this.preguntaExamenRepository = preguntaExamenRepository;
        this.usuarioEnExamenRepository = usuarioEnExamenRepository;
    }

    public ExamenDTO getExamenByIdSeccion(Integer idSeccion) {
        ExamenEntity examen = examenRepository.findBySeccion_IdSeccion(idSeccion)
                .orElseThrow(() -> new RuntimeException("No se encontró examen para la sección ID: " + idSeccion));

        return new ExamenDTO(
                examen.getIdExamen(),
                examen.getSeccion().getIdSeccion(),
                examen.getTitulo(),
                examen.getDescripcion(),
                examen.getPuntosAumento(),
                false
        );
    }

    public List<PreguntaExamenDTO> getPreguntasByExamen(Integer idExamen) {
        return preguntaExamenRepository.findByIdExamen(idExamen)
                .stream()
                .map(q -> new PreguntaExamenDTO(
                        q.getIdPreguntaExamen(),
                        q.getIdExamen(),
                        q.getTipo(),
                        q.getEnunciado(),
                        q.getOpcionA(),
                        q.getOpcionB(),
                        q.getOpcionC(),
                        q.getRespuestaCorrecta(),
                        q.getCodigoInicial(),
                        q.getSalidaEsperada(),
                        q.getPuntos()
                ))
                .collect(Collectors.toList());
    }

    public void completarExamen(Integer idExamen, Integer idUsuario) {
        // Validar si ya existe el registro para no duplicar
        boolean yaCompletado = usuarioEnExamenRepository.existsByIdUsuarioAndIdExamen(idUsuario, idExamen);

        if (!yaCompletado) {
            UsuarioEnExamenEntity registro = new UsuarioEnExamenEntity();
            registro.setIdExamen(idExamen);
            registro.setIdUsuario(idUsuario);
            usuarioEnExamenRepository.save(registro);
        }
    }
}