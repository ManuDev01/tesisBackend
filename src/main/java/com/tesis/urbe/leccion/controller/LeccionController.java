package com.tesis.urbe.leccion.controller;

import com.tesis.urbe.leccion.dto.LeccionDTO;
import com.tesis.urbe.leccion.service.LeccionService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
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

    @PatchMapping("/completeLeccion/{idLeccion}")
    public void completeLeccion(@PathVariable Integer idLeccion) {

    }

}
