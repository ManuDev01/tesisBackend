package com.tesis.urbe.proyectos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EvaluacionProyecto")
public class EvaluacionProyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idevaluacionproyecto")
    private Integer idEvaluacionProyecto;

    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "idproyecto")
    private Integer idProyecto;

    @Column(name = "codigo", columnDefinition = "TEXT")
    private String codigo;

    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "versionprompt")
    private String versionPrompt;

    @Column(name = "createat", insertable = false, updatable = false)
    private LocalDateTime createAt;

    public EvaluacionProyectoEntity() {}

    public Integer getIdEvaluacionProyecto() {
        return idEvaluacionProyecto;
    }

    public void setIdEvaluacionProyecto(Integer idEvaluacionProyecto) {
        this.idEvaluacionProyecto = idEvaluacionProyecto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVersionPrompt() {
        return versionPrompt;
    }

    public void setVersionPrompt(String versionPrompt) {
        this.versionPrompt = versionPrompt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
