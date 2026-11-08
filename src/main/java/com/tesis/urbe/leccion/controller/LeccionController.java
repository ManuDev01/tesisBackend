package com.tesis.urbe.leccion.controller;

import com.tesis.urbe.leccion.dto.*;
import com.tesis.urbe.leccion.service.LeccionEstadisticaService;
import com.tesis.urbe.leccion.service.LeccionService;
import com.tesis.urbe.medallas.dto.MedallaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leccion")
public class LeccionController {

    private final LeccionService leccionService;
    private final LeccionEstadisticaService leccionEstadisticaService;

    public LeccionController(LeccionService leccionService, LeccionEstadisticaService leccionEstadisticaService) {
        this.leccionService = leccionService;
        this.leccionEstadisticaService = leccionEstadisticaService;
    }

    @GetMapping("/getAllLeccions")
    public ResponseEntity<List<LeccionDTO>> getAllLeccions() {
        List<LeccionDTO> leccion = leccionService.getAllLeccions();
        return ResponseEntity.ok(leccion);
    }

    @GetMapping("/getLeccionByIdSeccion/{idSeccion}")
    public ResponseEntity<List<LeccionDTO>> getLeccionByIdSeccion(@PathVariable Integer idSeccion) {
        List<LeccionDTO> leccion = leccionService.getQuizzByIdSeccion(idSeccion);
        return ResponseEntity.ok(leccion);
    }

    @GetMapping("/getLeccionesByUsuario/{idUsuario}")
    public ResponseEntity<List<LeccionDTO>> getAllLeccionesByUsuario(@PathVariable Integer idUsuario) {
        List<LeccionDTO> lecciones = leccionService.getAllLeccionesByUsuario(idUsuario);
        return ResponseEntity.ok(lecciones);
    }

    @PatchMapping("/completeLeccion/{idLeccion}/user/{idUsuario}")
    public ResponseEntity<List<MedallaDTO>> completeLeccion(@PathVariable Integer idLeccion, @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(leccionService.completarLeccion(idLeccion, idUsuario));
    }

    @PostMapping("/registrarIntentoLeccion/{idLeccion}/user/{idUsuario}")
    public ResponseEntity<RegistroIntentoLeccionDTO> registrarIntentoLeccion(@PathVariable Integer idLeccion, @PathVariable Integer idUsuario) {
        // Inserta el intento en la tabla intento_leccion (idUsuario + idLeccion)
        return ResponseEntity.ok(leccionService.registrarIntentoLeccion(idLeccion, idUsuario));
    }

    @GetMapping("/intentos-por-leccion")
    public ResponseEntity<List<IntentosPorLeccionDTO>> getIntentosPorLeccion() {
        // Conteo total de intentos por lección desde intento_leccion
        // (solo lecciones con intentos, de mayor a menor; UNA sola consulta)
        return ResponseEntity.ok(leccionService.getIntentosPorLeccion());
    }

    @GetMapping("/completadas-por-usuario")
    public ResponseEntity<List<UsuarioLeccionesCompletadasDTO>> getLeccionesCompletadasPorUsuario() {
        return ResponseEntity.ok(leccionService.getLeccionesCompletadasPorUsuario());
    }

    @GetMapping("/mayores-tasas-error")
    public ResponseEntity<List<TasaErrorLeccionDTO>> getLeccionesConMayorTasaDeError() {
        return ResponseEntity.ok(leccionEstadisticaService.getLeccionesConMayorTasaDeError());
    }

    @GetMapping("/secciones-mayores-tasas-error")
    public ResponseEntity<List<TasaErrorSeccionDTO>> getSeccionesConMayorTasaDeError() {
        return ResponseEntity.ok(leccionEstadisticaService.getSeccionesConMayorTasaDeError());
    }

    @GetMapping("/tasa-error-por-usuario")
    public ResponseEntity<List<TasaErrorUsuarioDTO>> getTasaErrorPorUsuario() {
        return ResponseEntity.ok(leccionEstadisticaService.getTasaErrorPorUsuario());
    }


}