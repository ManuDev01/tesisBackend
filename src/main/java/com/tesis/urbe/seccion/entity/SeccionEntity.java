package com.tesis.urbe.seccion.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seccion")
public class SeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idseccion")
    private Integer idSeccion;

    @Column(name = "idcurso", nullable = false)
    private Integer idCurso;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosseccion")
    private Integer puntosSeccion;

    public SeccionEntity() {}

    public SeccionEntity(Integer idCurso, Integer orden, String titulo, String descripcion, String estado, Integer puntosSeccion) {
        this.idCurso = idCurso;
        this.orden = orden;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.puntosSeccion = puntosSeccion;
    }

    public Integer getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Integer idSeccion) {
        this.idSeccion = idSeccion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPuntosSeccion() {
        return puntosSeccion;
    }

    public void setPuntosSeccion(Integer puntosSeccion) {
        this.puntosSeccion = puntosSeccion;
    }
}