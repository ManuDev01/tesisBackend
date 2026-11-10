package com.tesis.urbe.quizz.controller;

import com.tesis.urbe.quizz.dto.FalloUsuarioQuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.dto.QuizzResultadoDTO;
import com.tesis.urbe.quizz.dto.ResponderQuizzDTO;
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

    /**
     * Devuelve los quizzes de varias lecciones en una sola petición
     * ({@code ?idsLecciones=1,2,3}): evita que el frontend haga una consulta por lección.
     */
    @GetMapping("/getQuizzByLecciones")
    public ResponseEntity<List<QuizzDTO>> getQuizzByLecciones(
            @RequestParam(required = false) List<Integer> idsLecciones) {
        return ResponseEntity.ok(quizzService.getQuizzByIdLecciones(idsLecciones));
    }

    /**
     * Registra las respuestas de un quiz, las evalúa y devuelve, además del
     * resultado, las medallas que se desbloquearon con esta tanda.
     */
    @PostMapping("/responder")
    public ResponseEntity<QuizzResultadoDTO> responderQuizz(@RequestBody ResponderQuizzDTO request) {
        return ResponseEntity.ok(quizzService.responderQuizz(request));
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
    public ResponseEntity<List<QuizzFalloDashboardDTO>> getQuizzConMasFallos() {
        List<QuizzFalloDashboardDTO> quizzConMasFallos = quizzService.getQuizzConMasFallos();
        return ResponseEntity.ok(quizzConMasFallos);
    }
}
