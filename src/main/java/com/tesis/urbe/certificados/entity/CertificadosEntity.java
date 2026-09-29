package com.tesis.urbe.certificados.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
public class CertificadosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcertificado")
    private Integer idCertificado;

    @Column(name = "idusuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idcurso", nullable = false)
    private Integer idCurso;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fechaemision", insertable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "\"uidCertificado\"", unique = true)
    private String uidCertificado;

    public CertificadosEntity() {}

    public CertificadosEntity(Integer idUsuario, Integer idCurso, String titulo, String descripcion, String uidCertificado) {
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.uidCertificado = uidCertificado;
    }

    public Integer getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Integer idCertificado) {
        this.idCertificado = idCertificado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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