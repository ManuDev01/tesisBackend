package com.tesis.urbe.quizz.controller;

import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzResultadoDTO;
import com.tesis.urbe.quizz.dto.ResponderQuizzDTO;
import com.tesis.urbe.quizz.service.QuizzService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Registra las respuestas de un quiz, las evalúa y devuelve, además del
     * resultado, las medallas que se desbloquearon con esta tanda.
     */
    @PostMapping("/responder")
    public ResponseEntity<QuizzResultadoDTO> responderQuizz(@RequestBody ResponderQuizzDTO request) {
        return ResponseEntity.ok(quizzService.responderQuizz(request));
    }
}
