package com.tesis.urbe.proyectos.controller;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.service.ProyectosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectosController {

    @Autowired
    private final ProyectosService proyectosService;

    public ProyectosController(ProyectosService proyectosService) {
        this.proyectosService = proyectosService;
    }

    @GetMapping("/getProyectos")
    public ResponseEntity<List<ProyectosDTO>> getProyectos() {
        List<ProyectosDTO> proyectos = proyectosService.getProyectos();
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/getProyectosLibres/{idUsuario}")
    public ResponseEntity<List<ProyectosDTO>> getProyectosLibres(@PathVariable Integer idUsuario) {
        List<ProyectosDTO> proyectosLibres = proyectosService.getProyectosLibresByUsuario(idUsuario);
        return ResponseEntity.ok(proyectosLibres);
    }

    @PostMapping("/completarProyecto/{idUsuario}/{idProyecto}")
    public ResponseEntity<Void> postUsuarioConProyecto(@PathVariable Integer idUsuario, @PathVariable Integer idProyecto) {
        this.proyectosService.saveUsuarioConProyecto(idUsuario, idProyecto);
        return ResponseEntity.ok().build();
    }

}
