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
    @Column(name = "idProyecto")
    private Integer idProyecto;

    @OneToOne
    @JoinColumn(name = "idSeccion", referencedColumnName = "idSeccion")
    private SeccionEntity idSeccion;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "decripcion")
    private String descripcion;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "puntosProyectos")
    private Integer puntosProyectos;

    @OneToOne
    @JoinColumn(name = "idCasoDeUso", referencedColumnName = "idCasoDeUso")
    private CasosDeUsoEntity idCasoDeUso;

    @Column(name = "createdat")
    private Date createAt;

    ProyectosEntity() {

    }

    public ProyectosEntity(Integer idProyecto, SeccionEntity idSeccion, String titulo, String descripcion, String codigo, String estado, Integer puntosProyectos, CasosDeUsoEntity idCasoDeUso, Date createAt) {
        this.idProyecto = idProyecto;
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.estado = estado;
        this.puntosProyectos = puntosProyectos;
        this.idCasoDeUso = idCasoDeUso;
        this.createAt = createAt;
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
}
