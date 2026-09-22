package com.tesis.urbe.leccion.dto;

import com.tesis.urbe.leccion.entity.LeccionEntity;

public class LeccionDTO {

    private Integer idLeccion;
    private Integer idSeccion;
    private String titulo;
    private String descripcion;
    private String estado;
    private Integer puntosLeccion;
    private boolean completada; // Nuevo campo

    public LeccionDTO() {}

    public LeccionDTO(Integer idLeccion, Integer idSeccion, String titulo, String descripcion, String estado, Integer puntosLeccion, boolean completada) {
        this.idLeccion = idLeccion;
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.puntosLeccion = puntosLeccion;
        this.completada = completada;
    }

    public static LeccionDTO fromEntity(LeccionEntity entity) {
        return fromEntity(entity, false);
    }

    public static LeccionDTO fromEntity(LeccionEntity entity, boolean completada) {
        return new LeccionDTO(
                entity.getIdLeccion(),
                entity.getIdSeccion() != null ? entity.getIdSeccion().getIdSeccion() : null,
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getPuntosLeccion(),
                completada
        );
    }

    // Getters y Setters
    public Integer getIdLeccion() { return idLeccion; }
    public void setIdLeccion(Integer idLeccion) { this.idLeccion = idLeccion; }

    public Integer getIdSeccion() { return idSeccion; }
    public void setIdSeccion(Integer idSeccion) { this.idSeccion = idSeccion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getPuntosLeccion() { return puntosLeccion; }
    public void setPuntosLeccion(Integer puntosLeccion) { this.puntosLeccion = puntosLeccion; }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }
}