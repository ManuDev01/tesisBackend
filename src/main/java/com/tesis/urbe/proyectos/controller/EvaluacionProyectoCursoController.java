package com.tesis.urbe.proyectos.controller;

import com.tesis.urbe.proyectos.dto.EvaluacionRequestDTO;
import com.tesis.urbe.proyectos.entity.EvaluacionProyectoCursoEntity;
import com.tesis.urbe.proyectos.service.EvaluacionProyectoCursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluarProyectoCurso/{idUsuario}")
public class EvaluacionProyectoCursoController {

    private final EvaluacionProyectoCursoService evaluacionService;

    public EvaluacionProyectoCursoController(EvaluacionProyectoCursoService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @PostMapping("/{proyectoCursoId}/evaluaciones")
    public ResponseEntity<EvaluacionProyectoCursoEntity> registrarEvaluacion(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoCursoId,
            @RequestBody EvaluacionRequestDTO request) {

        EvaluacionProyectoCursoEntity nuevaEvaluacion = evaluacionService.guardarEvaluacion(
                idUsuario,
                proyectoCursoId,
                request.codigo(),
                request.resultado(),
                request.modelo(),
                request.versionPrompt()
        );

        return ResponseEntity.ok(nuevaEvaluacion);
    }

    @GetMapping("/{proyectoCursoId}/evaluaciones")
    public ResponseEntity<List<EvaluacionProyectoCursoEntity>> listarEvaluaciones(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoCursoId) {

        List<EvaluacionProyectoCursoEntity> historial = evaluacionService.obtenerHistorial(idUsuario, proyectoCursoId);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/{proyectoCursoId}/evaluaciones/ultima")
    public ResponseEntity<EvaluacionProyectoCursoEntity> obtenerUltima(
            @PathVariable Integer idUsuario,
            @PathVariable Integer proyectoCursoId) {

        return evaluacionService.obtenerUltimaEvaluacion(idUsuario, proyectoCursoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
