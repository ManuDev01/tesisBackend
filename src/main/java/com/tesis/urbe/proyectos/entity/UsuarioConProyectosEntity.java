package com.tesis.urbe.proyectos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarioconproyecto")
public class UsuarioConProyectosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioconproyecto")
    private Long idUsuarioConProyecto;

    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "idproyecto")
    private Integer idProyecto;

    public UsuarioConProyectosEntity() {}

    public UsuarioConProyectosEntity(Integer idUsuario, Integer idProyecto) {
        this.idUsuario = idUsuario;
        this.idProyecto = idProyecto;
    }

    public Long getIdUsuarioConProyecto() {
        return idUsuarioConProyecto;
    }

    public void setIdUsuarioConProyecto(Long idUsuarioConProyecto) {
        this.idUsuarioConProyecto = idUsuarioConProyecto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }
}