package com.tesis.urbe.casosDeUso.entity;

import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "casosdeuso")
public class CasosDeUsoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcasodeuso")
    private Integer idCasoDeUso;

    @OneToOne
    @JoinColumn(name = "idproyecto", referencedColumnName = "idproyecto")
    private ProyectosEntity idProyecto;

    @Column(name = "entrada")
    private String entrada;

    @Column(name = "salida_esperada")
    private String salidaEsperada;

    @Column(name = "esoculto")
    private Boolean esOculto;

    public CasosDeUsoEntity() {}

    public CasosDeUsoEntity(Integer idCasoDeUso, ProyectosEntity idProyecto, String entrada, String salidaEsperada, Boolean esOculto) {
        this.idCasoDeUso = idCasoDeUso;
        this.idProyecto = idProyecto;
        this.entrada = entrada;
        this.salidaEsperada = salidaEsperada;
        this.esOculto = esOculto;
    }

    public Integer getIdCasoDeUso() {
        return idCasoDeUso;
    }

    public void setIdCasoDeUso(Integer idCasoDeUso) {
        this.idCasoDeUso = idCasoDeUso;
    }

    public ProyectosEntity getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(ProyectosEntity idProyecto) {
        this.idProyecto = idProyecto;
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