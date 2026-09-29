package com.tesis.urbe.proyectos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarioenproyectocurso")
public class UsuarioEnProyectoCursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioenproyectocurso")
    private Integer idUsuarioEnProyectoCurso;

    @Column(name = "idusuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idproyectocurso", nullable = false)
    private Integer idProyectoCurso;

    @Column(name = "completado", nullable = false)
    private Boolean completado = true;

    @Column(name = "fechacompletado", insertable = false, updatable = false)
    private LocalDateTime fechaCompletado;

    public UsuarioEnProyectoCursoEntity() {}

    public UsuarioEnProyectoCursoEntity(Integer idUsuario, Integer idProyectoCurso) {
        this.idUsuario = idUsuario;
        this.idProyectoCurso = idProyectoCurso;
        this.completado = true;
    }

    public Integer getIdUsuarioEnProyectoCurso() {
        return idUsuarioEnProyectoCurso;
    }

    public void setIdUsuarioEnProyectoCurso(Integer idUsuarioEnProyectoCurso) {
        this.idUsuarioEnProyectoCurso = idUsuarioEnProyectoCurso;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProyectoCurso() {
        return idProyectoCurso;
    }

    public void setIdProyectoCurso(Integer idProyectoCurso) {
        this.idProyectoCurso = idProyectoCurso;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }
}