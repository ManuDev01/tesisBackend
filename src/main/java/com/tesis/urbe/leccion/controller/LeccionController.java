package com.tesis.urbe.leccion.controller;

import com.tesis.urbe.leccion.dto.LeccionDTO;
import com.tesis.urbe.leccion.service.LeccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leccion")
public class LeccionController {

    private final LeccionService leccionService;

    public LeccionController(LeccionService leccionService) {
        this.leccionService = leccionService;
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
    public ResponseEntity<Void> completeLeccion(@PathVariable Integer idLeccion, @PathVariable Integer idUsuario) {
        leccionService.completarLeccion(idLeccion, idUsuario);
        return ResponseEntity.ok().build();
    }


}