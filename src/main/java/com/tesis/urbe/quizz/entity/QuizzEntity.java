package com.tesis.urbe.quizz.entity;

import com.tesis.urbe.leccion.entity.LeccionEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "quizz")
public class QuizzEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idquizz")
    private Integer idQuizz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idleccion", referencedColumnName = "idLeccion", nullable = false)
    private LeccionEntity idLeccion;

    @Column(name = "pregunta", nullable = false, columnDefinition = "TEXT")
    private String pregunta;

    @Column(name = "opcion_a", nullable = false)
    private String opcionA;

    @Column(name = "opcion_b", nullable = false)
    private String opcionB;

    @Column(name = "opcion_c", nullable = false)
    private String opcionC;

    @Column(name = "respuesta_correcta", nullable = false, columnDefinition = "bpchar")
    private String respuestaCorrecta;

    @Column(name = "puntosintento")
    private Integer puntosIntento;

    public QuizzEntity() {
    }

    public QuizzEntity(Integer idQuizz, LeccionEntity idLeccion, String pregunta, String opcionA, String opcionB, String opcionC, String respuestaCorrecta, Integer puntosIntento) {
        this.idQuizz = idQuizz;
        this.idLeccion = idLeccion;
        this.pregunta = pregunta;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.respuestaCorrecta = respuestaCorrecta;
        this.puntosIntento = puntosIntento;
    }

    public Integer getIdQuizz() {
        return idQuizz;
    }

    public void setIdQuizz(Integer idQuizz) {
        this.idQuizz = idQuizz;
    }

    public LeccionEntity getIdLeccion() {
        return idLeccion;
    }

    public void setIdLeccion(LeccionEntity idLeccion) {
        this.idLeccion = idLeccion;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOpcionA() {
        return opcionA;
    }

    public void setOpcionA(String opcionA) {
        this.opcionA = opcionA;
    }

    public String getOpcionB() {
        return opcionB;
    }

    public void setOpcionB(String opcionB) {
        this.opcionB = opcionB;
    }

    public String getOpcionC() {
        return opcionC;
    }

    public void setOpcionC(String opcionC) {
        this.opcionC = opcionC;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public Integer getPuntosIntento() {
        return puntosIntento;
    }

    public void setPuntosIntento(Integer puntosIntento) {
        this.puntosIntento = puntosIntento;
    }
}