package com.tesis.urbe.proyectos.entity;

import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
public class ProyectosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproyecto")
    private Integer idProyecto;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosproyectos")
    private Integer puntosProyectos;

    @Column(name = "dificultad")
    private String dificultad; // "FACIL", "MEDIO", "DIFICIL"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcasodeuso", referencedColumnName = "idcasodeuso")
    private CasosDeUsoEntity casoDeUso;

    @Column(name = "createdat", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public ProyectosEntity() {}

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
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

    public Integer getPuntosProyectos() {
        return puntosProyectos;
    }

    public void setPuntosProyectos(Integer puntosProyectos) {
        this.puntosProyectos = puntosProyectos;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public CasosDeUsoEntity getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasosDeUsoEntity casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}