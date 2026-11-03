package com.tesis.urbe.medallas.controller;

import com.tesis.urbe.medallas.dto.MedallaDTO;
import com.tesis.urbe.medallas.dto.UsuarioMedallaCountDTO;
import com.tesis.urbe.medallas.service.LogroService;
import com.tesis.urbe.medallas.service.MedallaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medallas")
public class MedallaController {

    private final MedallaService medallaService;
    private final LogroService logroService;

    public MedallaController(MedallaService medallaService, LogroService logroService) {
        this.medallaService = medallaService;
        this.logroService = logroService;
    }

    @GetMapping("/obtenerMedallas/{idUsuario}")
    public ResponseEntity<List<MedallaDTO>> getMedallasByUsuario(@PathVariable Integer idUsuario) {
        List<MedallaDTO> medallas = medallaService.getMedallasByUsuario(idUsuario);
        return ResponseEntity.ok(medallas);
    }

    @GetMapping("/rankingMedallas")
    public ResponseEntity<List<UsuarioMedallaCountDTO>> getRankingUsuariosConMasMedallas() {
        List<UsuarioMedallaCountDTO> ranking = medallaService.getRankingUsuariosConMasMedallas();
        return ResponseEntity.ok(ranking);
    }

    @PostMapping("/desbloquearMedalla/{idUsuario}/{idMedalla}")
    public ResponseEntity<Void> obtenerMedalla(@PathVariable Integer idUsuario, @PathVariable Integer idMedalla) {
        this.medallaService.guardarUsuarioConMedalla(idUsuario, idMedalla);
        return ResponseEntity.ok().build();
    }

    /**
     * Revisa todo el progreso histórico del usuario y desbloquea las medallas
     * que ya cumplía antes de que existiera la verificación automática.
     *
     * @return las medallas que se desbloquearon en esta pasada.
     */
    @PostMapping("/recalcular/{idUsuario}")
    public ResponseEntity<List<MedallaDTO>> recalcular(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(logroService.recalcular(idUsuario));
    }
}