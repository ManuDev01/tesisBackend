package com.tesis.urbe.leccion.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarioenleccion")
public class UsuarioEnLeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioenleccion")
    private Integer idUsuarioEnLeccion;

    @Column(name = "idleccion")
    private Integer idLeccion;

    @Column(name = "idusuario")
    private Integer idUsuario;

    public UsuarioEnLeccionEntity() {}

    public UsuarioEnLeccionEntity(Integer idLeccion, Integer idUsuario) {
        this.idLeccion = idLeccion;
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuarioEnLeccion() {
        return idUsuarioEnLeccion;
    }

    public void setIdUsuarioEnLeccion(Integer idUsuarioEnLeccion) {
        this.idUsuarioEnLeccion = idUsuarioEnLeccion;
    }

    public Integer getIdLeccion() {
        return idLeccion;
    }

    public void setIdLeccion(Integer idLeccion) {
        this.idLeccion = idLeccion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}