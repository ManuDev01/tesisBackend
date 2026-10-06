package com.tesis.urbe.casosDeUso.controller;

import com.tesis.urbe.casosDeUso.dto.CasoDeUsoProyectoCursoDTO;
import com.tesis.urbe.casosDeUso.dto.CasosDeUsoDTO;
import com.tesis.urbe.casosDeUso.service.CasosDeUsoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casos-de-uso")
public class CasosDeUsoController {

    private final CasosDeUsoService casosDeUsoService;

    public CasosDeUsoController(CasosDeUsoService casosDeUsoService) {
        this.casosDeUsoService = casosDeUsoService;
    }

    @GetMapping("/proyecto-curso/visibles/{idProyectoCurso}")
    public ResponseEntity<List<CasoDeUsoProyectoCursoDTO>> getCasosDeUsoVisiblesByProyectoCurso(@PathVariable Integer idProyectoCurso) {
        List<CasoDeUsoProyectoCursoDTO> casosVisibles = casosDeUsoService.obtenerCasosVisiblesProyectoCurso(idProyectoCurso);
        return ResponseEntity.ok(casosVisibles);
    }

    @GetMapping("/proyectoLibre/visibles/{idProyecto}")
    public ResponseEntity<List<CasosDeUsoDTO>> getCasosDeUsoVisiblesByProyecto(@PathVariable Integer idProyecto) {
        List<CasosDeUsoDTO> casosVisibles = casosDeUsoService.obtenerCasosVisiblesProyecto(idProyecto);
        return ResponseEntity.ok(casosVisibles);
    }
}