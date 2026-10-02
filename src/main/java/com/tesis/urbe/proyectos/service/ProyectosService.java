package com.tesis.urbe.proyectos.service;

import com.tesis.urbe.proyectos.dto.ProyectoCursoDTO;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.entity.ProyectoCursoEntity;
import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import com.tesis.urbe.proyectos.repository.ProyectoCursoRepository;
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
    private final ProyectoCursoRepository proyectoCursoRepository;

    public ProyectosService(ProyectosRepository proyectosRepository,
                            UsuarioConProyectoRepository usuarioConProyectoRepository,
                            ProyectoCursoRepository proyectoCursoRepository) {
        this.proyectosRepository = proyectosRepository;
        this.usuarioConProyectoRepository = usuarioConProyectoRepository;
        this.proyectoCursoRepository = proyectoCursoRepository;
    }

    public List<ProyectosDTO> getProyectos() {
        return proyectosRepository.findAll()
                .stream()
                .map(proyecto -> ProyectosDTO.fromEntity(proyecto, false))
                .collect(Collectors.toList());
    }

    public List<ProyectosDTO> getProyectosLibresByUsuario(Integer idUsuario) {
        Set<Integer> proyectosCompletadosIds = usuarioConProyectoRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(UsuarioConProyectosEntity::getIdProyecto)
                .collect(Collectors.toSet());

        return proyectosRepository.findAll()
                .stream()
                .map(proyecto -> {
                    boolean estaCompletado = proyectosCompletadosIds.contains(proyecto.getIdProyecto());
                    return ProyectosDTO.fromEntity(proyecto, estaCompletado);
                })
                .collect(Collectors.toList());
    }

    public ProyectoCursoDTO getProyectoCursoByCursoId(Integer idCurso) {
        ProyectoCursoEntity entity = proyectoCursoRepository.findByIdCurso(idCurso)
                .orElse(null);
        return ProyectoCursoDTO.fromEntity(entity);
    }

    public void saveUsuarioConProyecto(Integer idUsuario, Integer idProyecto) {
        UsuarioConProyectosEntity entity = new UsuarioConProyectosEntity(idUsuario, idProyecto);
        usuarioConProyectoRepository.save(entity);
    }
}