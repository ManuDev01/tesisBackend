package com.tesis.urbe.seccion.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "seccion")
public class SeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSeccion")
    private Integer idSeccion;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosSeccion")
    private Integer puntosSeccion;

    public SeccionEntity() {

    }

    public SeccionEntity(Integer idSeccion, String titulo, String descripcion, String estado, Integer puntosSeccion) {
        this.idSeccion = idSeccion;
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
