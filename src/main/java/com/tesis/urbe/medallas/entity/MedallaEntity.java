package com.tesis.urbe.medallas.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "medallas")
public class MedallaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMedalla")
    private Integer idMedalla;

    @Column(name = "nombreMedalla")
    private String nombreMedalla;

    @Column(name = "descripcionMedalla")
    private String descripcionMedalla;

    @Column(name = "valorMedalla")
    private Integer valorMedalla;

    @Column(name = "createAt")
    private Date createAt;

    MedallaEntity() {

    }

    public MedallaEntity(Integer idMedalla, String nombreMedalla, String descripcionMedalla, Integer valorMedalla, Date createAt) {
        this.idMedalla = idMedalla;
        this.nombreMedalla = nombreMedalla;
        this.descripcionMedalla = descripcionMedalla;
        this.valorMedalla = valorMedalla;
        this.createAt = createAt;
    }


    public Integer getIdMedalla() {
        return idMedalla;
    }

    public void setIdMedalla(Integer idMedalla) {
        this.idMedalla = idMedalla;
    }

    public String getNombreMedalla() {
        return nombreMedalla;
    }

    public void setNombreMedalla(String nombreMedalla) {
        this.nombreMedalla = nombreMedalla;
    }

    public String getDescripcionMedalla() {
        return descripcionMedalla;
    }

    public void setDescripcionMedalla(String descripcionMedalla) {
        this.descripcionMedalla = descripcionMedalla;
    }

    public Integer getValorMedalla() {
        return valorMedalla;
    }

    public void setValorMedalla(Integer valorMedalla) {
        this.valorMedalla = valorMedalla;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
