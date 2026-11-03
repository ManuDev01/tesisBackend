package com.tesis.urbe.proyectos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluacionproyectocurso")
public class EvaluacionProyectoCursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idevaluacionproyectocurso")
    private Integer idEvaluacionProyectoCurso;

    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "idproyectocurso")
    private Integer idProyectoCurso;

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

    public EvaluacionProyectoCursoEntity() {}

    public Integer getIdEvaluacionProyectoCurso() {
        return idEvaluacionProyectoCurso;
    }

    public void setIdEvaluacionProyectoCurso(Integer idEvaluacionProyectoCurso) {
        this.idEvaluacionProyectoCurso = idEvaluacionProyectoCurso;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProyectoCurso() {
        return idProyectoCurso;
    }

    public void setIdProyectoCurso(Integer idProyectoCurso) {
        this.idProyectoCurso = idProyectoCurso;
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

    public void setResultados(String resultado) {
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
