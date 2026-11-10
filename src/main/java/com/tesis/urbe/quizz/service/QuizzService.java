package com.tesis.urbe.quizz.service;

import com.tesis.urbe.leccion.entity.IntentoLeccionEntity;
import com.tesis.urbe.leccion.entity.LeccionEntity;
import com.tesis.urbe.leccion.repository.IntentoLeccionRepository;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.service.LogroService;
import com.tesis.urbe.quizz.dto.FalloUsuarioQuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.dto.QuizzResultadoDTO;
import com.tesis.urbe.quizz.dto.ResponderQuizzDTO;
import com.tesis.urbe.quizz.dto.RespuestaQuizzDTO;
import com.tesis.urbe.quizz.entity.FalloUsuarioQuizzEntity;
import com.tesis.urbe.quizz.entity.QuizzEntity;
import com.tesis.urbe.quizz.repository.FalloUsuarioQuizzRepository;
import com.tesis.urbe.quizz.repository.QuizzRepository;
import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizzService {

    private final QuizzRepository quizzRepository;
    private final IntentoLeccionRepository intentoLeccionRepository;
    private final LogroService logroService;
    private final FalloUsuarioQuizzRepository falloUsuarioQuizzRepository;
    private final UserRepository userRepository;

    public QuizzService(QuizzRepository quizzRepository,
                        IntentoLeccionRepository intentoLeccionRepository,
                        LogroService logroService,
                        FalloUsuarioQuizzRepository falloUsuarioQuizzRepository,
                        UserRepository userRepository) {
        this.quizzRepository = quizzRepository;
        this.intentoLeccionRepository = intentoLeccionRepository;
        this.logroService = logroService;
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

    /**
     * Devuelve los quizzes de varias lecciones en UNA sola consulta: el
     * frontend los pide en lote para evitar una petición por lección (N+1).
     */
    public List<QuizzDTO> getQuizzByIdLecciones(List<Integer> idsLecciones) {
        if (idsLecciones == null || idsLecciones.isEmpty()) {
            return List.of();
        }
        return quizzRepository.findByIdLeccionInLeccionId(idsLecciones)
                .stream()
                .map(QuizzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Evalúa las respuestas del estudiante contra la respuesta correcta
     * guardada en la base de datos, registra cada intento y dispara las
     * medallas asociadas al quiz.
     *
     * <p>Los fallos previos de la lección se calculan <strong>antes</strong> de
     * persistir esta tanda: la verificación de medallas corre en una transacción
     * independiente y no vería los registros aún no confirmados.
     */
    @Transactional
    public QuizzResultadoDTO responderQuizz(ResponderQuizzDTO request) {
        if (request == null || request.idUsuario() == null || request.idLeccion() == null) {
            throw new IllegalArgumentException("Debes indicar el usuario y la lección del quiz");
        }
        if (request.respuestas() == null || request.respuestas().isEmpty()) {
            throw new IllegalArgumentException("Debes enviar al menos una respuesta");
        }

        List<QuizzEntity> preguntas = quizzRepository.findByIdLeccion_IdLeccion(request.idLeccion());
        if (preguntas.isEmpty()) {
            throw new IllegalArgumentException("La lección no tiene preguntas de quiz registradas");
        }

        Map<Integer, QuizzEntity> preguntasPorId = new LinkedHashMap<>();
        preguntas.forEach(pregunta -> preguntasPorId.put(pregunta.getIdQuizz(), pregunta));

        Integer idSeccion = obtenerIdSeccion(preguntas.get(0));
        if (idSeccion == null) {
            throw new IllegalStateException("La lección del quiz no tiene una sección asociada");
        }

        // Fallos ya registrados en tandas anteriores de ESTA lección. Se calcula
        // ANTES de persistir la tanda actual: la verificación de medallas corre en
        // una transacción independiente y no vería los registros sin confirmar.
        int fallosPrevios = (int) intentoLeccionRepository
                .contarFallosDeLeccion(request.idUsuario(), request.idLeccion());

        int aciertos = 0;
        int fallos = 0;
        Set<Integer> respondidas = new HashSet<>();
        List<QuizzResultadoDTO.DetalleRespuestaDTO> detalle = new ArrayList<>();

        for (RespuestaQuizzDTO respuesta : request.respuestas()) {
            if (respuesta == null || respuesta.idQuizz() == null) {
                continue;
            }

            QuizzEntity pregunta = preguntasPorId.get(respuesta.idQuizz());
            if (pregunta == null) {
                continue;
            }

            // Si llegan dos respuestas para la misma pregunta, solo cuenta la primera.
            if (!respondidas.add(pregunta.getIdQuizz())) {
                continue;
            }

            boolean correcta = respuestasIguales(pregunta.getRespuestaCorrecta(), respuesta.respuesta());
            if (correcta) {
                aciertos++;
            } else {
                fallos++;
            }

            IntentoLeccionEntity intento = new IntentoLeccionEntity(
                    request.idUsuario(),
                    request.idLeccion(),
                    idSeccion,
                    correcta
            );
            intentoLeccionRepository.save(intento);

            detalle.add(new QuizzResultadoDTO.DetalleRespuestaDTO(
                    pregunta.getIdQuizz(),
                    correcta,
                    respuesta.respuesta(),
                    pregunta.getRespuestaCorrecta()
            ));
        }

        List<MedallaDTO> medallas = logroService.onQuizzRespondido(
                request.idUsuario(),
                request.idLeccion(),
                preguntas.size(),
                aciertos,
                fallos,
                fallosPrevios,
                request.sinFallos()
        );

        return new QuizzResultadoDTO(
                request.idLeccion(),
                preguntas.size(),
                respondidas.size(),
                aciertos,
                fallos,
                detalle,
                medallas
        );
    }

    private Integer obtenerIdSeccion(QuizzEntity pregunta) {
        LeccionEntity leccion = pregunta.getIdLeccion();
        if (leccion == null || leccion.getIdSeccion() == null) {
            return null;
        }
        return leccion.getIdSeccion().getIdSeccion();
    }

    private boolean respuestasIguales(String esperada, String enviada) {
        return normalizarRespuesta(esperada).equals(normalizarRespuesta(enviada));
    }

    private String normalizarRespuesta(String respuesta) {
        if (respuesta == null) {
            return "";
        }
        return respuesta.trim().toUpperCase(Locale.ROOT);
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

    /**
     * Preguntas de quiz agrupadas por fallos, ordenadas de mayor a menor para
     * el dashboard del admin. Se devuelven TODAS las filas (antes se limitaba
     * a una sola con {@code PageRequest.of(0, 1)}).
     */
    public List<QuizzFalloDashboardDTO> getQuizzConMasFallos() {
        return falloUsuarioQuizzRepository.findQuizzConMasFallos();
    }
}
