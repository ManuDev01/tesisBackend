package com.tesis.urbe.examen.service;

import com.tesis.urbe.examen.dto.ExamenDTO;
import com.tesis.urbe.examen.dto.PreguntaExamenDTO;
import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity;
import com.tesis.urbe.examen.repository.ExamenRepository;
import com.tesis.urbe.examen.repository.PreguntaExamenRepository;
import com.tesis.urbe.examen.repository.UsuarioEnExamenRepository;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.service.LogroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final PreguntaExamenRepository preguntaExamenRepository;
    private final UsuarioEnExamenRepository usuarioEnExamenRepository;
    private final LogroService logroService;

    public ExamenService(ExamenRepository examenRepository, PreguntaExamenRepository preguntaExamenRepository, UsuarioEnExamenRepository usuarioEnExamenRepository, LogroService logroService) {
        this.examenRepository = examenRepository;
        this.preguntaExamenRepository = preguntaExamenRepository;
        this.usuarioEnExamenRepository = usuarioEnExamenRepository;
        this.logroService = logroService;
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
        // Examen + preguntas en UNA sola consulta (EntityGraph). Si el examen no
        // existe se devuelve la lista vacía en lugar de un error 500.
        ExamenEntity examen = examenRepository.findByIdExamen(idExamen)
            .orElse(null);

        if (examen == null) {
            return List.of();
        }

        return examen.getPreguntas()
            .stream()
            .map(q -> new PreguntaExamenDTO(
                q.getIdPreguntaExamen(),
                examen.getIdExamen(),
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

    public List<MedallaDTO> completarExamen(Integer idExamen, Integer idUsuario, boolean sinFallos) {
        // Validar si ya existe el registro para no duplicar
        boolean yaCompletado = usuarioEnExamenRepository.existsByIdUsuarioAndIdExamen(idUsuario, idExamen);

        if (!yaCompletado) {
            UsuarioEnExamenEntity registro = new UsuarioEnExamenEntity();
            registro.setIdExamen(idExamen);
            registro.setIdUsuario(idUsuario);
            usuarioEnExamenRepository.save(registro);
        }

        // Se verifica siempre para permitir, por ejemplo, desbloquear la
        // medalla de examen perfecto en un reintento posterior.
        return logroService.onExamenCompletado(idUsuario, idExamen, sinFallos);
    }

    public List<UsuarioEnExamenEntity> getExamenesByUsuario(Integer idUsuario) {
        return usuarioEnExamenRepository.findByIdUsuario(idUsuario);
    }
}
