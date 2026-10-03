package com.tesis.urbe.examen.controller;

import com.tesis.urbe.examen.dto.ExamenDTO;
import com.tesis.urbe.examen.dto.PreguntaExamenDTO;
import com.tesis.urbe.examen.service.ExamenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examen")
public class ExamenController {

    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @GetMapping("/getExamenByIdSeccion/{idSeccion}")
    public ResponseEntity<ExamenDTO> getExamenByIdSeccion(@PathVariable Integer idSeccion) {
        ExamenDTO examen = examenService.getExamenByIdSeccion(idSeccion);
        return ResponseEntity.ok(examen);
    }

    @GetMapping("/getPreguntasByExamen/{idExamen}")
    public ResponseEntity<List<PreguntaExamenDTO>> getPreguntasByExamen(@PathVariable Integer idExamen) {
        List<PreguntaExamenDTO> preguntas = examenService.getPreguntasByExamen(idExamen);
        return ResponseEntity.ok(preguntas);
    }

    @PostMapping("/completarExamen/{idExamen}/user/{idUsuario}")
    public ResponseEntity<Void> completarExamen(@PathVariable Integer idExamen, @PathVariable Integer idUsuario) {
        examenService.completarExamen(idExamen, idUsuario);
        return ResponseEntity.ok().build();
    }
}