package com.tesis.urbe.quizz.controller;

import com.tesis.urbe.proyectos.dto.ProyectosDTO;
import com.tesis.urbe.quizz.dto.QuizzDTO;
import com.tesis.urbe.quizz.service.QuizzService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
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
}
