package com.tesis.urbe.examen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pregunta_examen")
public class PreguntaExamenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpregunta_examen")
    private Integer idPreguntaExamen;

    @Column(name = "idexamen", nullable = false)
    private Integer idExamen;

    @Column(name = "numero_pregunta")
    private Integer numeroPregunta;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "enunciado", nullable = false)
    private String enunciado;

    @Column(name = "opcion_a")
    private String opcionA;

    @Column(name = "opcion_b")
    private String opcionB;

    @Column(name = "opcion_c")
    private String opcionC;

    @Column(name = "respuesta_correcta")
    private String respuestaCorrecta;

    @Column(name = "codigo_inicial")
    private String codigoInicial;

    @Column(name = "salida_esperada")
    private String salidaEsperada;

    @Column(name = "puntos")
    private Integer puntos;

    public PreguntaExamenEntity() {}

    // Getters y Setters
    public Integer getIdPreguntaExamen() { return idPreguntaExamen; }
    public void setIdPreguntaExamen(Integer idPreguntaExamen) { this.idPreguntaExamen = idPreguntaExamen; }

    public Integer getIdExamen() { return idExamen; }
    public void setIdExamen(Integer idExamen) { this.idExamen = idExamen; }

    public Integer getNumeroPregunta() { return numeroPregunta; }
    public void setNumeroPregunta(Integer numeroPregunta) { this.numeroPregunta = numeroPregunta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getOpcionA() { return opcionA; }
    public void setOpcionA(String opcionA) { this.opcionA = opcionA; }

    public String getOpcionB() { return opcionB; }
    public void setOpcionB(String opcionB) { this.opcionB = opcionB; }

    public String getOpcionC() { return opcionC; }
    public void setOpcionC(String opcionC) { this.opcionC = opcionC; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getCodigoInicial() { return codigoInicial; }
    public void setCodigoInicial(String codigoInicial) { this.codigoInicial = codigoInicial; }

    public String getSalidaEsperada() { return salidaEsperada; }
    public void setSalidaEsperada(String salidaEsperada) { this.salidaEsperada = salidaEsperada; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }
}