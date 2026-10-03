package com.tesis.urbe.examen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarioenexamen")
public class UsuarioEnExamenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuarioenexamen")
    private Integer idUsuarioEnExamen;

    @Column(name = "idusuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idexamen", nullable = false)
    private Integer idExamen;

    public UsuarioEnExamenEntity() {}

    // Getters y Setters
    public Integer getIdUsuarioEnExamen() {
        return idUsuarioEnExamen;
    }

    public void setIdUsuarioEnExamen(Integer idUsuarioEnExamen) {
        this.idUsuarioEnExamen = idUsuarioEnExamen;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }
}