package com.tesis.urbe.casosDeUso.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "casodeusoproyectocurso")
public class CasoDeUsoProyectoCursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcasodeusoproyectocurso")
    private Integer idCasoDeUsoProyectoCurso;

    @Column(name = "idproyectocurso", nullable = false)
    private Integer idProyectoCurso;

    @Column(name = "entrada")
    private String entrada;

    @Column(name = "salida_esperada")
    private String salidaEsperada;

    @Column(name = "esoculto")
    private Boolean esOculto = false;

    public CasoDeUsoProyectoCursoEntity() {}

    public CasoDeUsoProyectoCursoEntity(Integer idProyectoCurso, String entrada, String salidaEsperada, Boolean esOculto) {
        this.idProyectoCurso = idProyectoCurso;
        this.entrada = entrada;
        this.salidaEsperada = salidaEsperada;
        this.esOculto = esOculto;
    }

    public Integer getIdCasoDeUsoProyectoCurso() {
        return idCasoDeUsoProyectoCurso;
    }

    public void setIdCasoDeUsoProyectoCurso(Integer idCasoDeUsoProyectoCurso) {
        this.idCasoDeUsoProyectoCurso = idCasoDeUsoProyectoCurso;
    }

    public Integer getIdProyectoCurso() {
        return idProyectoCurso;
    }

    public void setIdProyectoCurso(Integer idProyectoCurso) {
        this.idProyectoCurso = idProyectoCurso;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalidaEsperada() {
        return salidaEsperada;
    }

    public void setSalidaEsperada(String salidaEsperada) {
        this.salidaEsperada = salidaEsperada;
    }

    public Boolean getEsOculto() {
        return esOculto;
    }

    public void setEsOculto(Boolean esOculto) {
        this.esOculto = esOculto;
    }
}