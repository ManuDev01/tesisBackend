package com.tesis.urbe.proyectos.service;


import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import com.tesis.urbe.proyectos.repository.ProyectosRepository;
import com.tesis.urbe.proyectos.repository.UsuarioConProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProyectosService {

    private final ProyectosRepository proyectosRepository;
    private final UsuarioConProyectoRepository usuarioConProyectoRepository;

    public ProyectosService(ProyectosRepository proyectosRepository, UsuarioConProyectoRepository usuarioConProyectoRepository) {
        this.proyectosRepository = proyectosRepository;
        this.usuarioConProyectoRepository = usuarioConProyectoRepository;
    }

    public List<ProyectosDTO> getProyectos() {
        return proyectosRepository.findAll()
                .stream()
                .map(proyecto -> ProyectosDTO.fromEntity(proyecto, false))
                .collect(Collectors.toList());
    }

    public List<ProyectosDTO> getProyectosLibresByUsuario(Integer idUsuario) {
        // 1. Obtener IDs de proyectos que el usuario ya completó
        Set<Integer> proyectosCompletadosIds = usuarioConProyectoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConProyectosEntity::getIdProyecto)
                .collect(Collectors.toSet());

        // 2. Traer los proyectos de la sección 8 y marcar completado = true / false
        return proyectosRepository.findByIdSeccion_IdSeccion(8)
                .stream()
                .map(proyecto -> {
                    boolean estaCompletado = proyectosCompletadosIds.contains(proyecto.getIdProyecto());
                    return ProyectosDTO.fromEntity(proyecto, estaCompletado);
                })
                .collect(Collectors.toList());
    }

    public void saveUsuarioConProyecto(Integer idUsuario, Integer idProyecto) {
        UsuarioConProyectosEntity entity = new UsuarioConProyectosEntity(idUsuario, idProyecto);
        usuarioConProyectoRepository.save(entity);
    }

}
