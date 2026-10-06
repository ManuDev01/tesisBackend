package com.tesis.urbe.leccion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "intentoleccion")
public class IntentoLeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idintentoleccion")
    private Integer idIntentoLeccion;

    @Column(name = "idusuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idleccion", nullable = false)
    private Integer idLeccion;

    @Column(name = "idseccion", nullable = false)
    private Integer idSeccion;

    @Column(name = "escorrecto", nullable = false)
    private Boolean esCorrecto;

    @Column(name = "createdat", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public IntentoLeccionEntity() {}

    public IntentoLeccionEntity(Integer idUsuario, Integer idLeccion, Integer idSeccion, Boolean esCorrecto) {
        this.idUsuario = idUsuario;
        this.idLeccion = idLeccion;
        this.idSeccion = idSeccion;
        this.esCorrecto = esCorrecto;
    }

    public Integer getIdIntentoLeccion() { return idIntentoLeccion; }
    public void setIdIntentoLeccion(Integer idIntentoLeccion) { this.idIntentoLeccion = idIntentoLeccion; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdLeccion() { return idLeccion; }
    public void setIdLeccion(Integer idLeccion) { this.idLeccion = idLeccion; }

    public Integer getIdSeccion() { return idSeccion; }
    public void setIdSeccion(Integer idSeccion) { this.idSeccion = idSeccion; }

    public Boolean getEsCorrecto() { return esCorrecto; }
    public void setEsCorrecto(Boolean esCorrecto) { this.esCorrecto = esCorrecto; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}