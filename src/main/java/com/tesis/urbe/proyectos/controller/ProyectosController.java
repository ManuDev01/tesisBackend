package com.tesis.urbe.proyectos.controller;
import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.proyectos.service.ProyectosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

}
