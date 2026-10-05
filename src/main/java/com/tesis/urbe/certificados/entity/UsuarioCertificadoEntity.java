package com.tesis.urbe.certificados.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_certificados")
public class UsuarioCertificadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuariocertificado")
    private Integer idUsuarioCertificado;

    @Column(name = "idusuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idcertificado", nullable = false)
    private Integer idCertificado;

    @Column(name = "fechaemision", insertable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "\"uidCertificado\"", unique = true, nullable = false)
    private String uidCertificado;

    public UsuarioCertificadoEntity() {}

    public UsuarioCertificadoEntity(Integer idUsuario, Integer idCertificado, String uidCertificado) {
        this.idUsuario = idUsuario;
        this.idCertificado = idCertificado;
        this.uidCertificado = uidCertificado;
    }

    public Integer getIdUsuarioCertificado() {
        return idUsuarioCertificado;
    }

    public void setIdUsuarioCertificado(Integer idUsuarioCertificado) {
        this.idUsuarioCertificado = idUsuarioCertificado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Integer idCertificado) {
        this.idCertificado = idCertificado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUidCertificado() {
        return uidCertificado;
    }

    public void setUidCertificado(String uidCertificado) {
        this.uidCertificado = uidCertificado;
    }
}