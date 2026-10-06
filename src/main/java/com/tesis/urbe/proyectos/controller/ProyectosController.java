package com.tesis.urbe.proyectos.controller;

import com.tesis.urbe.proyectos.dto.ProyectoCursoDTO;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.dto.UsuarioProyectosRankingDTO;
import com.tesis.urbe.proyectos.service.ProyectosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectosController {

    private final ProyectosService proyectosService;

    @Autowired
    public ProyectosController(ProyectosService proyectosService) {
        this.proyectosService = proyectosService;
    }

    @GetMapping("/getProyectos")
    public ResponseEntity<List<ProyectosDTO>> getProyectos() {
        List<ProyectosDTO> proyectos = proyectosService.getProyectos();
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/getProyectoById/{idProyecto}")
    public ResponseEntity<ProyectosDTO> getProyectoById(@PathVariable Integer idProyecto) {
        ProyectosDTO proyecto = proyectosService.getProyectoById(idProyecto);
        if (proyecto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proyecto);
    }

    @GetMapping("/getProyectosLibres/{idUsuario}")
    public ResponseEntity<List<ProyectosDTO>> getProyectosLibres(@PathVariable Integer idUsuario) {
        List<ProyectosDTO> proyectosLibres = proyectosService.getProyectosLibresByUsuario(idUsuario);
        return ResponseEntity.ok(proyectosLibres);
    }

    @GetMapping("/getProyectoCurso/{idCurso}")
    public ResponseEntity<ProyectoCursoDTO> getProyectoCurso(@PathVariable Integer idCurso) {
        ProyectoCursoDTO proyecto = proyectosService.getProyectoCursoByCursoId(idCurso);
        if (proyecto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proyecto);
    }

    @GetMapping("/getProyectosCursoCompletados/{idUsuario}")
    public ResponseEntity<List<Integer>> getProyectosCursoCompletados(@PathVariable Integer idUsuario) {
        List<Integer> completados = proyectosService.getProyectosCursoCompletadosPorUsuario(idUsuario);
        return ResponseEntity.ok(completados);
    }

    @GetMapping("/rankingProyectos")
    public ResponseEntity<List<UsuarioProyectosRankingDTO>> getRankingProyectosPorUsuario() {
        List<UsuarioProyectosRankingDTO> ranking = proyectosService.getRankingProyectosPorUsuario();
        return ResponseEntity.ok(ranking);
    }

    @PostMapping("/completarProyecto/{idUsuario}/{idProyecto}")
    public ResponseEntity<Void> postUsuarioConProyecto(@PathVariable Integer idUsuario, @PathVariable Integer idProyecto) {
        this.proyectosService.saveUsuarioConProyecto(idUsuario, idProyecto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/completarProyectoCurso/{idUsuario}/{idProyectoCurso}")
    public ResponseEntity<Void> postUsuarioConProyectoCurso(@PathVariable Integer idUsuario, @PathVariable Integer idProyectoCurso) {
        this.proyectosService.saveUsuarioConProyectoCurso(idUsuario, idProyectoCurso);
        return ResponseEntity.ok().build();
    }
}