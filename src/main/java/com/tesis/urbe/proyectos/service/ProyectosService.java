package com.tesis.urbe.proyectos.service;

import com.tesis.urbe.proyectos.dto.ProyectoCursoDTO;
import com.tesis.urbe.proyectos.dto.ProyectoDetalleDTO;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.dto.UsuarioProyectosRankingDTO;
import com.tesis.urbe.proyectos.entity.ProyectoCursoEntity;
import com.tesis.urbe.proyectos.entity.UsuarioConProyectosEntity;
import com.tesis.urbe.proyectos.repository.ProyectoCursoRepository;
import com.tesis.urbe.proyectos.repository.ProyectosRepository;
import com.tesis.urbe.proyectos.repository.UsuarioConProyectoRepository;
import com.tesis.urbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProyectosService {

    private final ProyectosRepository proyectosRepository;
    private final UsuarioConProyectoRepository usuarioConProyectoRepository;
    private final ProyectoCursoRepository proyectoCursoRepository;
    private final UserRepository userRepository;

    public ProyectosService(ProyectosRepository proyectosRepository,
                            UsuarioConProyectoRepository usuarioConProyectoRepository,
                            ProyectoCursoRepository proyectoCursoRepository, UserRepository userRepository) {
        this.proyectosRepository = proyectosRepository;
        this.usuarioConProyectoRepository = usuarioConProyectoRepository;
        this.proyectoCursoRepository = proyectoCursoRepository;
        this.userRepository = userRepository;
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

    public List<UsuarioProyectosRankingDTO> getRankingProyectosPorUsuario() {
        List<UsuarioConProyectosEntity> todosLosProyectosCompletados = usuarioConProyectoRepository.findAll();

        Map<Integer, List<UsuarioConProyectosEntity>> proyectosPorUsuarioMap = todosLosProyectosCompletados.stream()
                .collect(Collectors.groupingBy(UsuarioConProyectosEntity::getIdUsuario));

        return userRepository.findAll().stream()
                .filter(user -> user.getIdRol() != null && user.getIdRol().getIdRol() != 2) // Excluir Admins
                .map(user -> {
                    List<UsuarioConProyectosEntity> registros = proyectosPorUsuarioMap.getOrDefault(user.getIdUsuario(), List.of());

                    List<Integer> idsProyectos = registros.stream()
                            .map(UsuarioConProyectosEntity::getIdProyecto)
                            .collect(Collectors.toList());

                    List<ProyectoDetalleDTO> proyectosDetalle = proyectosRepository.findByIdProyectoIn(idsProyectos)
                            .stream()
                            .map(p -> new ProyectoDetalleDTO(p.getIdProyecto(), p.getTitulo(), p.getDificultad()))
                            .collect(Collectors.toList());

                    String nombreCompleto = (user.getPrimerNombre() != null ? user.getPrimerNombre() : "") + " " +
                            (user.getPrimerApellido() != null ? user.getPrimerApellido() : "");

                    return new UsuarioProyectosRankingDTO(
                            user.getIdUsuario(),
                            nombreCompleto.trim().isEmpty() ? user.getNombreUsuario() : nombreCompleto.trim(),
                            (long) proyectosDetalle.size(),
                            proyectosDetalle
                    );
                })
                .sorted((u1, u2) -> u2.totalProyectosCompletados().compareTo(u1.totalProyectosCompletados()))
                .collect(Collectors.toList());
    }
}