package com.tesis.urbe.usuarioConMedalla.entity;

import com.tesis.urbe.user.entity.UserEntity;
import com.tesis.urbe.medallas.entity.MedallaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarioconmedalla")
public class UsuarioConMedallaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioconmedalla")
    private Integer idUsuarioConMedalla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false)
    private UserEntity idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmedalla", referencedColumnName = "idmedalla", nullable = false)
    private MedallaEntity idMedalla;

    @Column(name = "createat", insertable = false, updatable = false)
    private LocalDateTime createAt;

    public UsuarioConMedallaEntity() {
    }

    public UsuarioConMedallaEntity(Integer idUsuarioConMedalla, UserEntity idUsuario, MedallaEntity idMedalla, LocalDateTime createAt) {
        this.idUsuarioConMedalla = idUsuarioConMedalla;
        this.idUsuario = idUsuario;
        this.idMedalla = idMedalla;
        this.createAt = createAt;
    }

    public Integer getIdUsuarioConMedalla() {
        return idUsuarioConMedalla;
    }

    public void setIdUsuarioConMedalla(Integer idUsuarioConMedalla) {
        this.idUsuarioConMedalla = idUsuarioConMedalla;
    }

    public UserEntity getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UserEntity idUsuario) {
        this.idUsuario = idUsuario;
    }

    public MedallaEntity getIdMedalla() {
        return idMedalla;
    }

    public void setIdMedalla(MedallaEntity idMedalla) {
        this.idMedalla = idMedalla;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}