package com.tesis.urbe.medallas.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarioconmedalla")
public class UsuarioConMedallaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioconmedalla")
    private Integer idUsuarioConMedalla;

    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "idmedalla")
    private Integer idMedalla;

    @Column(name = "createat", insertable = false, updatable = false)
    private LocalDateTime createAt;

    public UsuarioConMedallaEntity() {}

    public UsuarioConMedallaEntity(Integer idUsuario, Integer idMedalla) {
        this.idUsuario = idUsuario;
        this.idMedalla = idMedalla;
    }

    public Integer getIdUsuarioConMedalla() {
        return idUsuarioConMedalla;
    }

    public void setIdUsuarioConMedalla(Integer idUsuarioConMedalla) {
        this.idUsuarioConMedalla = idUsuarioConMedalla;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdMedalla() {
        return idMedalla;
    }

    public void setIdMedalla(Integer idMedalla) {
        this.idMedalla = idMedalla;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createdAt) {
        this.createAt = createdAt;
    }
}