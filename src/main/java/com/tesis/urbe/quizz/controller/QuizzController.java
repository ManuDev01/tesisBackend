package com.tesis.urbe.quizz.controller;

<<<<<<< HEAD
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
=======
import com.tesis.urbe.quizz.dto.FalloUsuarioQuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.dto.QuizzFalloDashboardDTO;
import com.tesis.urbe.quizz.service.QuizzService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
>>>>>>> ccaeecbcb4756d06987af5f4d59fad9970223af0

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

<<<<<<< HEAD
    /**
     * Registra las respuestas de un quiz, las evalúa y devuelve, además del
     * resultado, las medallas que se desbloquearon con esta tanda.
     */
    @PostMapping("/responder")
    public ResponseEntity<QuizzResultadoDTO> responderQuizz(@RequestBody ResponderQuizzDTO request) {
        return ResponseEntity.ok(quizzService.responderQuizz(request));
    }
=======
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
>>>>>>> ccaeecbcb4756d06987af5f4d59fad9970223af0
}
