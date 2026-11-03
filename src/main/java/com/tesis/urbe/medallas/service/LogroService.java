package com.tesis.urbe.medallas.service;

import com.tesis.urbe.certificados.entity.CertificadosEntity;
import com.tesis.urbe.certificados.entity.UsuarioCertificadoEntity;
import com.tesis.urbe.certificados.repository.CertificadosRepository;
import com.tesis.urbe.certificados.repository.UsuarioCertificadoRepository;
import com.tesis.urbe.compiler.repository.RegistroCompilacionRepository;
import com.tesis.urbe.curso.entity.CursoEntity;
import com.tesis.urbe.curso.repository.CursoRepository;
import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity;
import com.tesis.urbe.examen.repository.ExamenRepository;
import com.tesis.urbe.examen.repository.UsuarioEnExamenRepository;
import com.tesis.urbe.leccion.entity.IntentoLeccionEntity;
import com.tesis.urbe.leccion.entity.LeccionEntity;
import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import com.tesis.urbe.leccion.repository.IntentoLeccionRepository;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.leccion.repository.UsuarioEnLeccionRepository;
import com.tesis.urbe.medallas.LogroMedalla;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.proyectos.entity.ProyectoCursoEntity;
import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import com.tesis.urbe.proyectos.entity.UsuarioEnProyectoCursoEntity;
import com.tesis.urbe.proyectos.repository.ProyectoCursoRepository;
import com.tesis.urbe.proyectos.repository.ProyectosRepository;
import com.tesis.urbe.proyectos.repository.UsuarioConProyectoRepository;
import com.tesis.urbe.proyectos.repository.UsuarioEnProyectoCursoRepository;
import com.tesis.urbe.seccion.entity.SeccionEntity;
import com.tesis.urbe.seccion.repository.SeccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Motor de desbloqueo de medallas.
 *
 * <p>Cada acción relevante del sistema (completar una lección, un examen, un
 * quiz, un proyecto, compilar código o emitir un certificado) llama al método
 * {@code on...} correspondiente. El servicio revisa las condiciones de las
 * medallas afectadas y las desbloquea de forma idempotente.
 *
 * <p>Todas las comprobaciones se ejecutan en una transacción independiente
 * ({@code REQUIRES_NEW}) y cualquier fallo se registra en el log sin romper la
 * acción principal del estudiante.
 */
@Service
public class LogroService {

    private static final Logger log = LoggerFactory.getLogger(LogroService.class);

    private final MedallaService medallaService;
    private final LeccionRepository leccionRepository;
    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final ExamenRepository examenRepository;
    private final UsuarioEnLeccionRepository usuarioEnLeccionRepository;
    private final UsuarioEnExamenRepository usuarioEnExamenRepository;
    private final IntentoLeccionRepository intentoLeccionRepository;
    private final ProyectosRepository proyectosRepository;
    private final ProyectoCursoRepository proyectoCursoRepository;
    private final UsuarioConProyectoRepository usuarioConProyectoRepository;
    private final UsuarioEnProyectoCursoRepository usuarioEnProyectoCursoRepository;
    private final RegistroCompilacionRepository registroCompilacionRepository;
    private final CertificadosRepository certificadosRepository;
    private final UsuarioCertificadoRepository usuarioCertificadoRepository;
    private final TransactionTemplate transactionTemplate;

    public LogroService(
            MedallaService medallaService,
            LeccionRepository leccionRepository,
            SeccionRepository seccionRepository,
            CursoRepository cursoRepository,
            ExamenRepository examenRepository,
            UsuarioEnLeccionRepository usuarioEnLeccionRepository,
            UsuarioEnExamenRepository usuarioEnExamenRepository,
            IntentoLeccionRepository intentoLeccionRepository,
            ProyectosRepository proyectosRepository,
            ProyectoCursoRepository proyectoCursoRepository,
            UsuarioConProyectoRepository usuarioConProyectoRepository,
            UsuarioEnProyectoCursoRepository usuarioEnProyectoCursoRepository,
            RegistroCompilacionRepository registroCompilacionRepository,
            CertificadosRepository certificadosRepository,
            UsuarioCertificadoRepository usuarioCertificadoRepository,
            PlatformTransactionManager transactionManager) {
        this.medallaService = medallaService;
        this.leccionRepository = leccionRepository;
        this.seccionRepository = seccionRepository;
        this.cursoRepository = cursoRepository;
        this.examenRepository = examenRepository;
        this.usuarioEnLeccionRepository = usuarioEnLeccionRepository;
        this.usuarioEnExamenRepository = usuarioEnExamenRepository;
        this.intentoLeccionRepository = intentoLeccionRepository;
        this.proyectosRepository = proyectosRepository;
        this.proyectoCursoRepository = proyectoCursoRepository;
        this.usuarioConProyectoRepository = usuarioConProyectoRepository;
        this.usuarioEnProyectoCursoRepository = usuarioEnProyectoCursoRepository;
        this.registroCompilacionRepository = registroCompilacionRepository;
        this.certificadosRepository = certificadosRepository;
        this.usuarioCertificadoRepository = usuarioCertificadoRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    // ------------------------------------------------------------------
    // Eventos del sistema
    // ------------------------------------------------------------------

    /**
     * Se invoca cuando el usuario completa una lección.
     * Medallas: 7 (lección), 6 (sección), 20-22 (inicio de curso), 15-18 (curso).
     */
    public List<MedallaDTO> onLeccionCompletada(Integer idUsuario, Integer idLeccion) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null || idLeccion == null) {
                return nuevas;
            }

            desbloquear(nuevas, idUsuario, LogroMedalla.LECCION_COMPLETADA);

            Set<Integer> lecciones = leccionesCompletadas(idUsuario);
            Set<Integer> examenes = examenesCompletados(idUsuario);

            LeccionEntity leccion = leccionRepository.findById(idLeccion).orElse(null);
            if (leccion != null && leccion.getIdSeccion() != null) {
                SeccionEntity seccion = leccion.getIdSeccion();
                if (seccionCompletada(seccion.getIdSeccion(), lecciones, examenes)) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.SECCION_COMPLETADA);
                }
                verificarCurso(nuevas, idUsuario, seccion.getIdCurso(), lecciones, examenes);
            }

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "lección completada");
    }

    /**
     * Se invoca cuando el usuario completa un examen.
     *
     * @param sinFallos indica si el examen se respondió sin ningún error.
     *                  Medallas: 8 (examen perfecto), 6 (sección), 20-22 y 15-18.
     */
    public List<MedallaDTO> onExamenCompletado(Integer idUsuario, Integer idExamen, boolean sinFallos) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null || idExamen == null) {
                return nuevas;
            }

            if (sinFallos) {
                desbloquear(nuevas, idUsuario, LogroMedalla.EXAMEN_PERFECTO);
            }

            Set<Integer> lecciones = leccionesCompletadas(idUsuario);
            Set<Integer> examenes = examenesCompletados(idUsuario);

            ExamenEntity examen = examenRepository.findById(idExamen).orElse(null);
            if (examen != null && examen.getSeccion() != null) {
                SeccionEntity seccion = examen.getSeccion();
                if (seccionCompletada(seccion.getIdSeccion(), lecciones, examenes)) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.SECCION_COMPLETADA);
                }
                verificarCurso(nuevas, idUsuario, seccion.getIdCurso(), lecciones, examenes);
            }

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "examen completado");
    }

    /**
     * Se invoca cuando el usuario responde un quiz de una lección.
     *
     * @param totalPreguntas cantidad de preguntas que tiene el quiz en la BD.
     * @param aciertos       respuestas correctas de esta tanda.
     * @param fallos         respuestas incorrectas de esta tanda.
     *                       Medallas: 4, 5 y 9; también el inicio del curso.
     */
    public List<MedallaDTO> onQuizzRespondido(Integer idUsuario, Integer idLeccion,
                                              int totalPreguntas, int aciertos, int fallos) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null || idLeccion == null) {
                return nuevas;
            }

            if (fallos > 0) {
                desbloquear(nuevas, idUsuario, LogroMedalla.FALLO_QUIZZ);
            }

            if (totalPreguntas >= 2 && fallos >= totalPreguntas) {
                desbloquear(nuevas, idUsuario, LogroMedalla.FALLOS_QUIZZ_COMPLETO);
            }

            if (totalPreguntas > 0 && fallos == 0 && aciertos >= totalPreguntas) {
                desbloquear(nuevas, idUsuario, LogroMedalla.QUIZZ_PERFECTO);
            }

            LeccionEntity leccion = leccionRepository.findById(idLeccion).orElse(null);
            if (leccion != null && leccion.getIdSeccion() != null) {
                Set<Integer> lecciones = leccionesCompletadas(idUsuario);
                Set<Integer> examenes = examenesCompletados(idUsuario);
                verificarCurso(nuevas, idUsuario, leccion.getIdSeccion().getIdCurso(), lecciones, examenes);
            }

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "quiz respondido");
    }

    /**
     * Se invoca cuando el usuario completa un proyecto libre o de curso.
     *
     * @param esProyectoCurso {@code true} si se completó un proyecto de curso,
     *                        {@code false} si es un desafío libre.
     *                        Medallas: 1, 3, 10, 11, 12, 13 y 14.
     */
    public List<MedallaDTO> onProyectoCompletado(Integer idUsuario, Integer idProyecto, boolean esProyectoCurso) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null || idProyecto == null) {
                return nuevas;
            }

            Set<Integer> libres = proyectosLibresCompletados(idUsuario);
            Set<Integer> deCurso = proyectosCursoCompletados(idUsuario);

            if (libres.size() + deCurso.size() >= 10) {
                desbloquear(nuevas, idUsuario, LogroMedalla.DIEZ_PROYECTOS);
            }

            if (esProyectoCurso) {
                ProyectoCursoEntity proyecto = proyectoCursoRepository.findById(idProyecto).orElse(null);
                if (proyecto != null) {
                    CursoEntity curso = cursoRepository.findById(proyecto.getIdCurso()).orElse(null);
                    if (curso != null && nivelDe(curso.getNombre()) == NivelCurso.BASICO) {
                        desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_PRINCIPIANTE);
                    }
                }
            } else {
                ProyectosEntity proyecto = proyectosRepository.findById(idProyecto).orElse(null);
                if (proyecto != null && dificultadDe(proyecto.getDificultad()) == Dificultad.FACIL) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_PRINCIPIANTE);
                }
                verificarDesafios(nuevas, idUsuario, libres);
            }

            String tipo = esProyectoCurso ? "CURSO" : "LIBRE";
            if (registroCompilacionRepository.contarIntentos(idUsuario, tipo, idProyecto) == 1
                    && registroCompilacionRepository.contarIntentosExitosos(idUsuario, tipo, idProyecto) == 1) {
                desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_UN_INTENTO);
            }

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "proyecto completado");
    }

    /**
     * Se invoca después de cada intento de compilación en el editor.
     * Medallas: 19 (primera compilación exitosa) y 23 (compilación vacía).
     */
    public List<MedallaDTO> onCompilacion(Integer idUsuario, String codigo,
                                          String tipoProyecto, Integer idProyecto, boolean compilado) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null) {
                return nuevas;
            }

            boolean codigoVacio = codigo == null || codigo.isBlank();
            registroCompilacionRepository.registrar(idUsuario, tipoProyecto, idProyecto, compilado, codigoVacio);

            if (codigoVacio) {
                desbloquear(nuevas, idUsuario, LogroMedalla.COMPILACION_VACIA);
            }
            if (compilado) {
                desbloquear(nuevas, idUsuario, LogroMedalla.PRIMERA_COMPILACION);
            }

            // También se comprueba aquí para que la medalla no dependa del orden
            // en que el frontend llame a "completar proyecto" y "compilar".
            verificarProyectoUnIntento(nuevas, idUsuario, tipoProyecto, idProyecto);

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "compilación");
    }

    /**
     * Se invoca cuando se emite un certificado de curso.
     * Medallas: 20-22 (inicio), 15-18 (curso completado) y 24.
     */
    public List<MedallaDTO> onCertificadoEmitido(Integer idUsuario, Integer idCurso) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null || idCurso == null) {
                return nuevas;
            }

            Set<Integer> lecciones = leccionesCompletadas(idUsuario);
            Set<Integer> examenes = examenesCompletados(idUsuario);
            verificarCurso(nuevas, idUsuario, idCurso, lecciones, examenes);
            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "certificado emitido");
    }

    /**
     * Recalcula todas las medallas que pueden deducirse del progreso ya
     * guardado. Sirve para que los usuarios que ya tenían avance antes de
     * existir este motor reciban sus logros.
     *
     * <p>No puede deducir la medalla 8 (examen perfecto) porque la tabla de
     * exámenes no guarda el detalle de aciertos/fallos.
     */
    public List<MedallaDTO> recalcular(Integer idUsuario) {
        return enTransaccion(() -> {
            List<MedallaDTO> nuevas = new ArrayList<>();
            if (idUsuario == null) {
                return nuevas;
            }

            Set<Integer> lecciones = leccionesCompletadas(idUsuario);
            Set<Integer> examenes = examenesCompletados(idUsuario);

            if (!lecciones.isEmpty()) {
                desbloquear(nuevas, idUsuario, LogroMedalla.LECCION_COMPLETADA);
            }

            for (CursoEntity curso : cursoRepository.findAll()) {
                for (SeccionEntity seccion : seccionRepository.findByIdCursoOrderByOrdenAsc(curso.getIdCurso())) {
                    if (seccionCompletada(seccion.getIdSeccion(), lecciones, examenes)) {
                        desbloquear(nuevas, idUsuario, LogroMedalla.SECCION_COMPLETADA);
                        break;
                    }
                }
            }

            Set<Integer> libres = proyectosLibresCompletados(idUsuario);
            Set<Integer> deCurso = proyectosCursoCompletados(idUsuario);

            if (libres.size() + deCurso.size() >= 10) {
                desbloquear(nuevas, idUsuario, LogroMedalla.DIEZ_PROYECTOS);
            }

            verificarDesafios(nuevas, idUsuario, libres);

            for (Integer idProyecto : libres) {
                ProyectosEntity proyecto = proyectosRepository.findById(idProyecto).orElse(null);
                if (proyecto != null && dificultadDe(proyecto.getDificultad()) == Dificultad.FACIL) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_PRINCIPIANTE);
                }
                if (registroCompilacionRepository.contarIntentos(idUsuario, "LIBRE", idProyecto) == 1
                        && registroCompilacionRepository.contarIntentosExitosos(idUsuario, "LIBRE", idProyecto) == 1) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_UN_INTENTO);
                }
            }

            for (Integer idProyectoCurso : deCurso) {
                ProyectoCursoEntity proyecto = proyectoCursoRepository.findById(idProyectoCurso).orElse(null);
                if (proyecto != null) {
                    CursoEntity curso = cursoRepository.findById(proyecto.getIdCurso()).orElse(null);
                    if (curso != null && nivelDe(curso.getNombre()) == NivelCurso.BASICO) {
                        desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_PRINCIPIANTE);
                    }
                }
                if (registroCompilacionRepository.contarIntentos(idUsuario, "CURSO", idProyectoCurso) == 1
                        && registroCompilacionRepository.contarIntentosExitosos(idUsuario, "CURSO", idProyectoCurso) == 1) {
                    desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_UN_INTENTO);
                }
            }

            for (CursoEntity curso : cursoRepository.findAll()) {
                if (cursoIniciado(curso.getIdCurso(), lecciones, examenes)) {
                    verificarCurso(nuevas, idUsuario, curso.getIdCurso(), lecciones, examenes);
                }
            }

            for (UsuarioCertificadoEntity relacion : usuarioCertificadoRepository.findByIdUsuario(idUsuario)) {
                CertificadosEntity certificado = certificadosRepository.findById(relacion.getIdCertificado()).orElse(null);
                if (certificado != null) {
                    verificarCurso(nuevas, idUsuario, certificado.getIdCurso(), lecciones, examenes);
                }
            }

            List<IntentoLeccionEntity> intentos = intentoLeccionRepository.findByIdUsuario(idUsuario);
            if (intentos.stream().anyMatch(intento -> Boolean.FALSE.equals(intento.getEsCorrecto()))) {
                desbloquear(nuevas, idUsuario, LogroMedalla.FALLO_QUIZZ);
            }

            if (registroCompilacionRepository.contarCompilaciones(idUsuario) > 0) {
                desbloquear(nuevas, idUsuario, LogroMedalla.PRIMERA_COMPILACION);
            }
            if (registroCompilacionRepository.existeCompilacionVacia(idUsuario)) {
                desbloquear(nuevas, idUsuario, LogroMedalla.COMPILACION_VACIA);
            }

            verificarTodas(nuevas, idUsuario);
            return nuevas;
        }, "recálculo de medallas");
    }

    // ------------------------------------------------------------------
    // Verificaciones internas
    // ------------------------------------------------------------------

    private void verificarCurso(List<MedallaDTO> nuevas,
                                Integer idUsuario,
                                Integer idCurso,
                                Set<Integer> lecciones,
                                Set<Integer> examenes) {
        if (idCurso == null) {
            return;
        }

        CursoEntity curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso == null) {
            return;
        }

        NivelCurso nivel = nivelDe(curso.getNombre());
        if (nivel != null) {
            desbloquear(nuevas, idUsuario, nivel.inicio);

            if (cursoCompletado(idCurso, lecciones, examenes)) {
                desbloquear(nuevas, idUsuario, nivel.fin);
            }
        }

        if (todosCursosCompletados(lecciones, examenes)) {
            desbloquear(nuevas, idUsuario, LogroMedalla.CURSOS_TODOS);
        }
    }

    private boolean seccionCompletada(Integer idSeccion,
                                      Set<Integer> leccionesCompletadas,
                                      Set<Integer> examenesCompletados) {
        if (idSeccion == null) {
            return false;
        }

        List<LeccionEntity> lecciones = leccionRepository.findByIdSeccion_IdSeccion(idSeccion);
        Optional<ExamenEntity> examen = examenRepository.findBySeccion_IdSeccion(idSeccion);

        if (lecciones.isEmpty() && examen.isEmpty()) {
            return false;
        }

        boolean todasLasLecciones = lecciones.stream()
                .allMatch(leccion -> leccionesCompletadas.contains(leccion.getIdLeccion()));
        if (!todasLasLecciones) {
            return false;
        }

        return examen
                .map(e -> examenesCompletados.contains(e.getIdExamen()))
                .orElse(true);
    }

    private boolean cursoCompletado(Integer idCurso,
                                    Set<Integer> leccionesCompletadas,
                                    Set<Integer> examenesCompletados) {
        List<SeccionEntity> secciones = seccionRepository.findByIdCursoOrderByOrdenAsc(idCurso);
        if (secciones.isEmpty()) {
            return false;
        }

        return secciones.stream()
                .allMatch(seccion -> seccionCompletada(seccion.getIdSeccion(),
                        leccionesCompletadas, examenesCompletados));
    }

    private boolean todosCursosCompletados(Set<Integer> leccionesCompletadas,
                                           Set<Integer> examenesCompletados) {
        List<CursoEntity> cursosConContenido = cursoRepository.findAll()
                .stream()
                .filter(curso -> !seccionRepository.findByIdCursoOrderByOrdenAsc(curso.getIdCurso()).isEmpty())
                .toList();

        if (cursosConContenido.isEmpty()) {
            return false;
        }

        return cursosConContenido.stream()
                .allMatch(curso -> cursoCompletado(curso.getIdCurso(), leccionesCompletadas, examenesCompletados));
    }

    private boolean cursoIniciado(Integer idCurso,
                                  Set<Integer> leccionesCompletadas,
                                  Set<Integer> examenesCompletados) {
        for (SeccionEntity seccion : seccionRepository.findByIdCursoOrderByOrdenAsc(idCurso)) {
            boolean leccionHecha = leccionRepository.findByIdSeccion_IdSeccion(seccion.getIdSeccion())
                    .stream()
                    .anyMatch(leccion -> leccionesCompletadas.contains(leccion.getIdLeccion()));
            if (leccionHecha) {
                return true;
            }

            Optional<ExamenEntity> examen = examenRepository.findBySeccion_IdSeccion(seccion.getIdSeccion());
            if (examen.isPresent() && examenesCompletados.contains(examen.get().getIdExamen())) {
                return true;
            }
        }
        return false;
    }

    private void verificarDesafios(List<MedallaDTO> nuevas, Integer idUsuario, Set<Integer> completados) {
        List<ProyectosEntity> todos = proyectosRepository.findAll();
        if (todos.isEmpty()) {
            return;
        }

        List<Integer> faciles = new ArrayList<>();
        List<Integer> intermedios = new ArrayList<>();
        List<Integer> dificiles = new ArrayList<>();

        for (ProyectosEntity proyecto : todos) {
            Dificultad dificultad = dificultadDe(proyecto.getDificultad());
            if (dificultad == null) {
                continue;
            }
            switch (dificultad) {
                case FACIL -> faciles.add(proyecto.getIdProyecto());
                case INTERMEDIO -> intermedios.add(proyecto.getIdProyecto());
                case DIFICIL -> dificiles.add(proyecto.getIdProyecto());
            }
        }

        if (!faciles.isEmpty() && completados.containsAll(faciles)) {
            desbloquear(nuevas, idUsuario, LogroMedalla.DESAFIOS_FACILES);
        }
        if (!intermedios.isEmpty() && completados.containsAll(intermedios)) {
            desbloquear(nuevas, idUsuario, LogroMedalla.DESAFIOS_INTERMEDIOS);
        }
        if (!dificiles.isEmpty() && completados.containsAll(dificiles)) {
            desbloquear(nuevas, idUsuario, LogroMedalla.DESAFIOS_DIFICILES);
        }

        Set<Integer> todosLosIds = todos.stream()
                .map(ProyectosEntity::getIdProyecto)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!todosLosIds.isEmpty() && completados.containsAll(todosLosIds)) {
            desbloquear(nuevas, idUsuario, LogroMedalla.DESAFIOS_TODOS);
        }
    }

    private void verificarProyectoUnIntento(List<MedallaDTO> nuevas,
                                            Integer idUsuario,
                                            String tipoProyecto,
                                            Integer idProyecto) {
        if (idUsuario == null || idProyecto == null || tipoProyecto == null) {
            return;
        }

        String tipo = tipoProyecto.trim().toUpperCase(Locale.ROOT);
        boolean completado;
        if ("CURSO".equals(tipo)) {
            completado = proyectosCursoCompletados(idUsuario).contains(idProyecto);
        } else if ("LIBRE".equals(tipo)) {
            completado = proyectosLibresCompletados(idUsuario).contains(idProyecto);
        } else {
            return;
        }

        if (completado
                && registroCompilacionRepository.contarIntentos(idUsuario, tipo, idProyecto) == 1
                && registroCompilacionRepository.contarIntentosExitosos(idUsuario, tipo, idProyecto) == 1) {
            desbloquear(nuevas, idUsuario, LogroMedalla.PROYECTO_UN_INTENTO);
        }
    }

    private void verificarTodas(List<MedallaDTO> nuevas, Integer idUsuario) {
        if (idUsuario == null) {
            return;
        }

        Set<Integer> obtenidas = medallaService.obtenerIdsMedallas(idUsuario);
        boolean tieneTodas = Arrays.stream(LogroMedalla.values())
                .filter(logro -> logro != LogroMedalla.TODAS_LAS_MEDALLAS)
                .allMatch(logro -> obtenidas.contains(logro.getId()));

        if (tieneTodas) {
            desbloquear(nuevas, idUsuario, LogroMedalla.TODAS_LAS_MEDALLAS);
        }
    }

    private void desbloquear(List<MedallaDTO> nuevas, Integer idUsuario, LogroMedalla logro) {
        if (idUsuario == null || logro == null) {
            return;
        }

        MedallaDTO medalla = medallaService.desbloquear(idUsuario, logro);
        if (medalla != null) {
            nuevas.add(medalla);
            log.info("Usuario {} desbloqueó la medalla {} ({})", idUsuario, logro.name(), logro.getId());
        }
    }

    private Set<Integer> leccionesCompletadas(Integer idUsuario) {
        return usuarioEnLeccionRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioEnLeccionEntity::getIdLeccion)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Integer> examenesCompletados(Integer idUsuario) {
        return usuarioEnExamenRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioEnExamenEntity::getIdExamen)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Integer> proyectosLibresCompletados(Integer idUsuario) {
        return usuarioConProyectoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConProyectosEntity::getIdProyecto)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Integer> proyectosCursoCompletados(Integer idUsuario) {
        return usuarioEnProyectoCursoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioEnProyectoCursoEntity::getIdProyectoCurso)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private List<MedallaDTO> enTransaccion(Supplier<List<MedallaDTO>> logica, String contexto) {
        try {
            List<MedallaDTO> resultado = transactionTemplate.execute(status -> logica.get());
            return resultado != null ? resultado : List.of();
        } catch (Exception e) {
            log.warn("No se pudieron verificar los logros ({}): {}", contexto, e.getMessage());
            return List.of();
        }
    }

    // ------------------------------------------------------------------
    // Normalización de nombres de curso y dificultad
    // ------------------------------------------------------------------

    private NivelCurso nivelDe(String nombreCurso) {
        String nombre = normalizar(nombreCurso);
        if (nombre.contains("basico") || nombre.contains("principiante")
                || nombre.contains("inicial") || nombre.contains("beginner")) {
            return NivelCurso.BASICO;
        }
        if (nombre.contains("intermedio") || nombre.contains("medio")
                || nombre.contains("intermediate")) {
            return NivelCurso.INTERMEDIO;
        }
        if (nombre.contains("avanzado") || nombre.contains("dificil")
                || nombre.contains("experto") || nombre.contains("advanced")) {
            return NivelCurso.AVANZADO;
        }
        return null;
    }

    private Dificultad dificultadDe(String dificultad) {
        String valor = normalizar(dificultad);
        if (valor.isEmpty()) {
            return null;
        }
        if (valor.contains("facil") || valor.contains("principiante")
                || valor.contains("basico") || valor.contains("easy")
                || valor.contains("beginner")) {
            return Dificultad.FACIL;
        }
        if (valor.contains("intermedio") || valor.contains("medio")
                || valor.contains("medium") || valor.contains("normal")) {
            return Dificultad.INTERMEDIO;
        }
        if (valor.contains("dificil") || valor.contains("avanzado")
                || valor.contains("hard") || valor.contains("experto")) {
            return Dificultad.DIFICIL;
        }
        return null;
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    private enum NivelCurso {
        BASICO(LogroMedalla.INICIO_CURSO_BASICO, LogroMedalla.CURSO_BASICO),
        INTERMEDIO(LogroMedalla.INICIO_CURSO_INTERMEDIO, LogroMedalla.CURSO_INTERMEDIO),
        AVANZADO(LogroMedalla.INICIO_CURSO_AVANZADO, LogroMedalla.CURSO_AVANZADO);

        private final LogroMedalla inicio;
        private final LogroMedalla fin;

        NivelCurso(LogroMedalla inicio, LogroMedalla fin) {
            this.inicio = inicio;
            this.fin = fin;
        }
    }

    private enum Dificultad {
        FACIL,
        INTERMEDIO,
        DIFICIL
    }
}
