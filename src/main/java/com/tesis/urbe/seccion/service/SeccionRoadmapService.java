package com.tesis.urbe.seccion.service;

import com.tesis.urbe.examen.entity.ExamenEntity;
import com.tesis.urbe.examen.repository.ExamenRepository;
import com.tesis.urbe.leccion.entity.LeccionEntity;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.leccion.repository.UsuarioEnLeccionRepository;
import com.tesis.urbe.seccion.dto.NodoCaminoDTO;
import com.tesis.urbe.seccion.dto.SeccionRoadmapDTO;
import com.tesis.urbe.seccion.entity.SeccionEntity;
import com.tesis.urbe.seccion.repository.SeccionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeccionRoadmapService {

    private final SeccionRepository seccionRepository;
    private final LeccionRepository leccionRepository;
    private final ExamenRepository examenRepository;
    private final UsuarioEnLeccionRepository usuarioEnLeccionRepository;

    public SeccionRoadmapService(SeccionRepository seccionRepository,
                                 LeccionRepository leccionRepository,
                                 ExamenRepository examenRepository,
                                 UsuarioEnLeccionRepository usuarioEnLeccionRepository) {
        this.seccionRepository = seccionRepository;
        this.leccionRepository = leccionRepository;
        this.examenRepository = examenRepository;
        this.usuarioEnLeccionRepository = usuarioEnLeccionRepository;
    }

    public List<SeccionRoadmapDTO> getRoadmapByUsuario(Integer idCurso, Integer idUsuario) {
        Set<Integer> leccionesCompletadas = usuarioEnLeccionRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(rel -> rel.getIdLeccion())
                .collect(Collectors.toSet());

        List<SeccionEntity> secciones = seccionRepository.findByIdCursoOrderByOrdenAsc(idCurso);
        List<SeccionRoadmapDTO> roadmap = new ArrayList<>();

        for (SeccionEntity seccion : secciones) {
            List<LeccionEntity> lecciones = leccionRepository.findByIdSeccion_IdSeccion(seccion.getIdSeccion());
            Optional<ExamenEntity> examenOpt = examenRepository.findBySeccion_IdSeccion(seccion.getIdSeccion());

            List<NodoCaminoDTO> nodos = new ArrayList<>();

            // 1. Agregar Lecciones de la Sección
            for (LeccionEntity l : lecciones) {
                boolean completada = leccionesCompletadas.contains(l.getIdLeccion());
                nodos.add(new NodoCaminoDTO(
                        l.getIdLeccion(),
                        "LECCION",
                        l.getTitulo(),
                        l.getDescripcion(),
                        l.getPuntosLeccion(),
                        completada,
                        true // Disponible para cursar
                ));
            }

            // 2. Agregar Examen al Final de las Lecciones
            if (examenOpt.isPresent()) {
                ExamenEntity examen = examenOpt.get();
                boolean todasLeccionesCompletadas = lecciones.stream()
                        .allMatch(l -> leccionesCompletadas.contains(l.getIdLeccion()));

                nodos.add(new NodoCaminoDTO(
                        examen.getIdExamen(),
                        "EXAMEN",
                        examen.getTitulo(),
                        examen.getDescripcion(),
                        examen.getPuntosAumento(),
                        false, // Mapear con tabla de resultados de examen si existe
                        true   // Siempre habilitado para permitir saltar sección
                ));
            }

            roadmap.add(new SeccionRoadmapDTO(
                    seccion.getIdSeccion(),
                    seccion.getTitulo(),
                    seccion.getOrden(),
                    nodos
            ));
        }

        return roadmap;
    }
}