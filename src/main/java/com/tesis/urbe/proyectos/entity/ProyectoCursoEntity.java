package com.tesis.urbe.proyectos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectocurso")
public class ProyectoCursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproyectocurso")
    private Integer idProyectoCurso;

    @Column(name = "idcurso", nullable = false)
    private Integer idCurso;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosproyecto")
    private Integer puntosProyecto;

    @Column(name = "createdat", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public ProyectoCursoEntity() {}

    public Integer getIdProyectoCurso() {
        return idProyectoCurso;
    }

    public void setIdProyectoCurso(Integer idProyectoCurso) {
        this.idProyectoCurso = idProyectoCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPuntosProyecto() {
        return puntosProyecto;
    }

    public void setPuntosProyecto(Integer puntosProyecto) {
        this.puntosProyecto = puntosProyecto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}