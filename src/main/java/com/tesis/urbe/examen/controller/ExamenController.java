package com.tesis.urbe.examen.controller;

import com.tesis.urbe.examen.dto.ExamenAntesDeSeccionDTO;
import com.tesis.urbe.examen.dto.ExamenDTO;
import com.tesis.urbe.examen.dto.PreguntaExamenDTO;
import com.tesis.urbe.examen.entity.UsuarioEnExamenEntity;
import com.tesis.urbe.examen.service.ExamenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tesis.urbe.examen.service.ExamenEstadisticaService;

import java.util.List;

@RestController
@RequestMapping("/examen")
public class ExamenController {

    private final ExamenService examenService;
    private final ExamenEstadisticaService examenEstadisticaService;

    public ExamenController(ExamenService examenService, ExamenEstadisticaService examenEstadisticaService) {
        this.examenService = examenService;
        this.examenEstadisticaService = examenEstadisticaService;
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

    @GetMapping("/getExamenesByUsuario/{idUsuario}")
    public ResponseEntity<List<UsuarioEnExamenEntity>> getExamenesByUsuario(@PathVariable Integer idUsuario) {
        List<UsuarioEnExamenEntity> examenes = examenService.getExamenesByUsuario(idUsuario);
        return ResponseEntity.ok(examenes);
    }

    @PostMapping("/completarExamen/{idExamen}/user/{idUsuario}")
    public ResponseEntity<Void> completarExamen(@PathVariable Integer idExamen, @PathVariable Integer idUsuario) {
        examenService.completarExamen(idExamen, idUsuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios-examen-incompleto")
    public ResponseEntity<List<ExamenAntesDeSeccionDTO>> getUsuariosExamenIncompleto() {
        return ResponseEntity.ok(examenEstadisticaService.getUsuariosExamenAntesDeCompletarSeccion());
    }
}