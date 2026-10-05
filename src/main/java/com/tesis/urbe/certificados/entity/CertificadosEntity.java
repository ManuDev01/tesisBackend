package com.tesis.urbe.certificados.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "certificados")
public class CertificadosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcertificado")
    private Integer idCertificado;

    @Column(name = "idcurso", nullable = false)
    private Integer idCurso;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    public CertificadosEntity() {}

    public CertificadosEntity(Integer idCurso, String titulo, String descripcion) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Integer getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Integer idCertificado) {
        this.idCertificado = idCertificado;
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
}