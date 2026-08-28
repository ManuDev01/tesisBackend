package com.tesis.urbe.proyectos.entity;

import com.tesis.urbe.casosDeUso.entity.CasosDeUsoEntity;
import com.tesis.urbe.seccion.entity.SeccionEntity;
import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "proyectos")
public class ProyectosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproyecto")
    private Integer idProyecto;

    @OneToOne
    @JoinColumn(name = "idseccion", referencedColumnName = "idseccion")
    private SeccionEntity idSeccion;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosproyectos")
    private Integer puntosProyectos;

    @OneToOne
    @JoinColumn(name = "idcasodeuso", referencedColumnName = "idcasodeuso")
    private CasosDeUsoEntity idCasoDeUso;

    @Column(name = "createdat")
    private Date createAt;

    @Column(name = "decripcion")
    private String decripcion;

    public ProyectosEntity() {}

    public ProyectosEntity(Integer idProyecto, SeccionEntity idSeccion, String titulo, String descripcion, String codigo, String estado, Integer puntosProyectos, CasosDeUsoEntity idCasoDeUso, Date createAt, String decripcion) {
        this.idProyecto = idProyecto;
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.estado = estado;
        this.puntosProyectos = puntosProyectos;
        this.idCasoDeUso = idCasoDeUso;
        this.createAt = createAt;
        this.decripcion = decripcion;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public SeccionEntity getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(SeccionEntity idSeccion) {
        this.idSeccion = idSeccion;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPuntosProyectos() {
        return puntosProyectos;
    }

    public void setPuntosProyectos(Integer puntosProyectos) {
        this.puntosProyectos = puntosProyectos;
    }

    public CasosDeUsoEntity getIdCasoDeUso() {
        return idCasoDeUso;
    }

    public void setIdCasoDeUso(CasosDeUsoEntity idCasoDeUso) {
        this.idCasoDeUso = idCasoDeUso;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
    }
}