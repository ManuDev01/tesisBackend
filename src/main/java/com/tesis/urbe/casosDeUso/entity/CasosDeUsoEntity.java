package com.tesis.urbe.casosDeUso.entity;

import com.tesis.urbe.proyectos.entity.ProyectosEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "casosDeUso")
public class CasosDeUsoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCasoDeUso")
    private Integer idCasoDeUso;

    @OneToOne
    @JoinColumn(name = "idProyecto", referencedColumnName = "idProyecto")
    private ProyectosEntity idProyecto;

    CasosDeUsoEntity() {}

    public CasosDeUsoEntity(Integer idCasoDeUso, ProyectosEntity idProyecto) {
        this.idCasoDeUso = idCasoDeUso;
        this.idProyecto = idProyecto;
    }

    public Integer getIdCasoDeUso() {
        return idCasoDeUso;
    }

    public void setIdCasoDeUso(Integer idCasoDeUso) {
        this.idCasoDeUso = idCasoDeUso;
    }

    public ProyectosEntity getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(ProyectosEntity idProyecto) {
        this.idProyecto = idProyecto;
    }
}
