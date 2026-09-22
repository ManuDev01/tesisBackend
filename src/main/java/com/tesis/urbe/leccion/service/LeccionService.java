package com.tesis.urbe.leccion.service;

import com.tesis.urbe.leccion.dto.LeccionDTO;
import com.tesis.urbe.leccion.entity.UsuarioEnLeccionEntity;
import com.tesis.urbe.leccion.repository.LeccionRepository;
import com.tesis.urbe.leccion.repository.UsuarioEnLeccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeccionService {

    private final LeccionRepository leccionRepository;
    private final UsuarioEnLeccionRepository usuarioEnLeccionRepository;

    public LeccionService(LeccionRepository leccionRepository, UsuarioEnLeccionRepository usuarioEnLeccionRepository) {
        this.leccionRepository = leccionRepository;
        this.usuarioEnLeccionRepository = usuarioEnLeccionRepository;
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

        // 2. Traer TODAS las lecciones y mapear completada = true / false
        return leccionRepository.findAll()
                .stream()
                .map(leccion -> {
                    boolean estaCompletada = leccionesCompletadasIds.contains(leccion.getIdLeccion());
                    return LeccionDTO.fromEntity(leccion, estaCompletada);
                })
                .collect(Collectors.toList());
    }

    public void completarLeccion(Integer idLeccion, Integer idUsuario) {
        boolean yaCompletada = usuarioEnLeccionRepository.existsByIdLeccionAndIdUsuario(idLeccion, idUsuario);

        if (!yaCompletada) {
            UsuarioEnLeccionEntity nuevaRelacion = new UsuarioEnLeccionEntity(idLeccion, idUsuario);
            usuarioEnLeccionRepository.save(nuevaRelacion);
        }
    }
}