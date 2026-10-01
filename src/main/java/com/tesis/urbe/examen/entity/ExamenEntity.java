package com.tesis.urbe.examen.entity;

import com.tesis.urbe.seccion.entity.SeccionEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "examen")
public class ExamenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idexamen")
    private Integer idExamen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idseccion", referencedColumnName = "idseccion", nullable = false)
    private SeccionEntity seccion;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "puntosaumento")
    private Integer puntosAumento;

    public ExamenEntity() {}

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public SeccionEntity getSeccion() {
        return seccion;
    }

    public void setSeccion(SeccionEntity seccion) {
        this.seccion = seccion;
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

    public Integer getPuntosAumento() {
        return puntosAumento;
    }

    public void setPuntosAumento(Integer puntosAumento) {
        this.puntosAumento = puntosAumento;
    }
}