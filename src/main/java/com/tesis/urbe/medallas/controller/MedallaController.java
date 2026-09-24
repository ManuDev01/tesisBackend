package com.tesis.urbe.medallas.controller;

import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.service.MedallaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medallas")
public class MedallaController {

    private final MedallaService medallaService;

    public MedallaController(MedallaService medallaService) {
        this.medallaService = medallaService;
    }

    @GetMapping("/obtenerMedallas/{idUsuario}")
    public ResponseEntity<List<MedallaDTO>> getMedallasByUsuario(@PathVariable Integer idUsuario) {
        List<MedallaDTO> medallas = medallaService.getMedallasByUsuario(idUsuario);
        return ResponseEntity.ok(medallas);
    }

    @PostMapping("/desbloquearMedalla/{idUsuario}/{idMedalla}")
    public ResponseEntity<Void> obtenerMedalla(@PathVariable Integer idUsuario, @PathVariable Integer idMedalla) {
        this.medallaService.guardarUsuarioConMedalla(idUsuario, idMedalla);
        return ResponseEntity.ok().build();
    }
}