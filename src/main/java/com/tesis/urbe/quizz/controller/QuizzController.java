package com.tesis.urbe.quizz.controller;

import com.tesis.urbe.quizz.dto.FalloUsuarioQuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.service.QuizzService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizz")
public class QuizzController {

    private final QuizzService quizzService;

    public QuizzController(QuizzService quizzService) {
        this.quizzService = quizzService;
    }

    @GetMapping("/getQuizz")
    public ResponseEntity<List<QuizzDTO>> getQuizz() {
        List<QuizzDTO> quizz = quizzService.getQuizz();
        return ResponseEntity.ok(quizz);
    }

    @GetMapping("/getQuizzByIdLeccion/{idLeccion}")
    public ResponseEntity<List<QuizzDTO>> getQuizzByIdLeccion(@PathVariable Integer idLeccion) {
        List<QuizzDTO> quizzList = quizzService.getQuizzByIdLeccion(idLeccion);
        return ResponseEntity.ok(quizzList);
    }

    @PostMapping("/saveFalloUsuarioQuizz")
    public ResponseEntity<FalloUsuarioQuizzDTO> saveFalloUsuarioQuizz(@RequestBody FalloUsuarioQuizzDTO falloUsuarioQuizzDTO) {
        FalloUsuarioQuizzDTO savedFallo = quizzService.saveFalloUsuarioQuizz(falloUsuarioQuizzDTO);
        return ResponseEntity.ok(savedFallo);
    }

    @GetMapping("/getFalloUsuarioQuizzByIdUsuario/{idUsuario}")
    public ResponseEntity<List<FalloUsuarioQuizzDTO>> getFalloUsuarioQuizzByIdUsuario(@PathVariable Integer idUsuario) {
        List<FalloUsuarioQuizzDTO> fallos = quizzService.getFalloUsuarioQuizzByIdUsuario(idUsuario);
        return ResponseEntity.ok(fallos);
    }

  @GetMapping("/getQuizzConMasFallos")
public ResponseEntity<QuizzFalloDashboardDTO> getQuizzConMasFallos() {
    QuizzFalloDashboardDTO quizzConMasFallos = quizzService.getQuizzConMasFallos();
    return ResponseEntity.ok(quizzConMasFallos);
}
}
