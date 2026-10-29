package com.tesis.urbe.proyectos.controller;

import com.tesis.urbe.proyectos.entity.EvaluacionProyectoEntity;
import com.tesis.urbe.proyectos.service.EvaluacionProyectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tesis.urbe.proyectos.dto.EvaluacionRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/evaluarProyecto/{idUsuario}")
public class EvaluacionProyectoController {

    private final EvaluacionProyectoService evaluacionService;

    public EvaluacionProyectoController(EvaluacionProyectoService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @PostMapping("/{proyectoId}/evaluaciones")
    public ResponseEntity<EvaluacionProyectoEntity> registrarEvaluacion(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoId,
            @RequestBody EvaluacionRequestDTO request) {

        EvaluacionProyectoEntity nuevaEvaluacion = evaluacionService.guardarEvaluacion(
                idUsuario,
                proyectoId,
                request.codigo(),
                request.resultado(),
                request.modelo(),
                request.versionPrompt()
        );

        return ResponseEntity.ok(nuevaEvaluacion);
    }

    @GetMapping("/{proyectoId}/evaluaciones")
    public ResponseEntity<List<EvaluacionProyectoEntity>> listarEvaluaciones(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoId) {

        List<EvaluacionProyectoEntity> historial = evaluacionService.obtenerHistorial(idUsuario, proyectoId);
        
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/{proyectoId}/evaluaciones/ultima")
    public ResponseEntity<EvaluacionProyectoEntity> obtenerUltima(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoId) {

        return evaluacionService.obtenerUltimaEvaluacion(idUsuario, proyectoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
