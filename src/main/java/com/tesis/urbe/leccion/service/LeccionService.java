package com.tesis.urbe.leccion.service;

import com.tesis.urbe.leccion.dto.LeccionDTO;
import com.tesis.urbe.leccion.dto.UsuarioLeccionesCompletadasDTO;
import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.leccion.repository.UsuarioEnLeccionRepository;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.service.LogroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeccionService {

    private final LeccionRepository leccionRepository;
    private final UsuarioEnLeccionRepository usuarioEnLeccionRepository;
    private final LogroService logroService;

    public LeccionService(LeccionRepository leccionRepository,
                          UsuarioEnLeccionRepository usuarioEnLeccionRepository,
                          LogroService logroService) {
        this.leccionRepository = leccionRepository;
        this.usuarioEnLeccionRepository = usuarioEnLeccionRepository;
        this.logroService = logroService;
    }

    public List<LeccionDTO> getAllLeccions() {
        return leccionRepository.findAll()
                .stream()
                .map(LeccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LeccionDTO> getQuizzByIdSeccion(Integer idSeccion) {
        return leccionRepository.findByIdSeccion_IdSeccion(idSeccion)
                .stream()
                .map(LeccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LeccionDTO> getAllLeccionesByUsuario(Integer idUsuario) {
        // 1. Obtener los IDs de las lecciones completadas por el usuario logueado
        Set<Integer> leccionesCompletadasIds = usuarioEnLeccionRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioEnLeccionEntity::getIdLeccion)
                .collect(Collectors.toSet());

        // 2. Traer TODAS las lecciones (con su sección ya cargada: sin el fetch,
        // mapear el DTO disparaba 1 consulta perezosa por sección = N+1)
        return leccionRepository.findAllConSeccion()
                .stream()
                .map(leccion -> {
                    boolean estaCompletada = leccionesCompletadasIds.contains(leccion.getIdLeccion());
                    return LeccionDTO.fromEntity(leccion, estaCompletada);
                })
                .collect(Collectors.toList());
    }

    public List<MedallaDTO> completarLeccion(Integer idLeccion, Integer idUsuario) {
        boolean yaCompletada = usuarioEnLeccionRepository.existsByIdLeccionAndIdUsuario(idLeccion, idUsuario);

        if (!yaCompletada) {
            UsuarioEnLeccionEntity nuevaRelacion = new UsuarioEnLeccionEntity(idLeccion, idUsuario);
            usuarioEnLeccionRepository.save(nuevaRelacion);
        }

        // Se verifica siempre: así una segunda llamada (o un reintento del
        // frontend) también puede desbloquear medallas pendientes.
        return logroService.onLeccionCompletada(idUsuario, idLeccion);
    }

    @Transactional(readOnly = true)
    public List<UsuarioLeccionesCompletadasDTO> getLeccionesCompletadasPorUsuario() {
        List<Object[]> filas = usuarioEnLeccionRepository.findResumenLeccionesCompletadasPorUsuarioRaw();
        List<UsuarioLeccionesCompletadasDTO> resultado = new ArrayList<>();

        for (Object[] fila : filas) {
            Integer idUsuario = ((Number) fila[0]).intValue();
            String usuario = (String) fila[1];
            Long leccionesCompletadas = ((Number) fila[2]).longValue();
            String leccionesRaw = (String) fila[3];

            List<UsuarioLeccionesCompletadasDTO.LeccionDetalleDTO> lecciones = new ArrayList<>();

            if (leccionesRaw != null && !leccionesRaw.isBlank()) {
                String[] items = leccionesRaw.split("\\|\\|\\|");
                for (String item : items) {
                    String[] partes = item.split(":::");
                    if (partes.length == 2) {
                        lecciones.add(new UsuarioLeccionesCompletadasDTO.LeccionDetalleDTO(
                                Integer.parseInt(partes[0]),
                                partes[1]
                        ));
                    }
                }
            }

            resultado.add(new UsuarioLeccionesCompletadasDTO(
                    idUsuario,
                    usuario,
                    leccionesCompletadas,
                    lecciones
            ));
        }

        return resultado;
    }
}