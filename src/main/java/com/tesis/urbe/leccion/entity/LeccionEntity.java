package com.tesis.urbe.leccion.entity;

import com.tesis.urbe.seccion.entity.SeccionEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "leccion")
public class LeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLeccion")
    private Integer idLeccion;

    @OneToOne
    @JoinColumn(name = "idSeccion", referencedColumnName = "idSeccion")
    private SeccionEntity idSeccion;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosLeccion")
    private Integer puntosLeccion;

    LeccionEntity() {

    }

    public LeccionEntity(Integer idLeccion, SeccionEntity idSeccion, String titulo, String descripcion, String estado, Integer puntosLeccion) {
        this.idLeccion = idLeccion;
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.puntosLeccion = puntosLeccion;
    }

    public Integer getIdLeccion() {
        return idLeccion;
    }

    public void setIdLeccion(Integer idLeccion) {
        this.idLeccion = idLeccion;
    }

    public SeccionEntity getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(SeccionEntity idSeccion) {
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

    public Integer getPuntosLeccion() {
        return puntosLeccion;
    }

    public void setPuntosLeccion(Integer puntosLeccion) {
        this.puntosLeccion = puntosLeccion;
    }
}
