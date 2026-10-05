package com.tesis.urbe.casosDeUso.controller;

import com.tesis.urbe.casosDeUso.dto.CasoDeUsoProyectoCursoDTO;
import com.tesis.urbe.casosDeUso.repository.CasoDeUsoProyectoCursoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casos-de-uso")
public class CasosDeUsoController {

    private final CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository;

    public CasosDeUsoController(CasoDeUsoProyectoCursoRepository casoDeUsoProyectoCursoRepository) {
        this.casoDeUsoProyectoCursoRepository = casoDeUsoProyectoCursoRepository;
    }

    @GetMapping("/proyecto-curso/visibles/{idProyectoCurso}")
    public ResponseEntity<List<CasoDeUsoProyectoCursoDTO>> getCasosDeUsoVisiblesByProyectoCurso(@PathVariable Integer idProyectoCurso) {
        List<CasoDeUsoProyectoCursoDTO> casosVisibles = casoDeUsoProyectoCursoRepository
                .findByIdProyectoCursoAndEsOcultoFalse(idProyectoCurso)
                .stream()
                .map(CasoDeUsoProyectoCursoDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(casosVisibles);
    }
}