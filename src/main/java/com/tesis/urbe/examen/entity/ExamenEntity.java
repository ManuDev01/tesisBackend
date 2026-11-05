package com.tesis.urbe.examen.entity;

import com.tesis.urbe.seccion.entity.SeccionEntity;
import jakarta.persistence.*;
import java.util.List;

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

        @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private List<PreguntaExamenEntity> preguntas;

        @Column(name = "titulo", nullable = false)
        private String titulo;

        @Column(name = "descripcion")
        private String descripcion;

        @Column(name = "puntosaumento")
        private Integer puntosAumento;

        public ExamenEntity() {}

        // Getters y Setters
        public Integer getIdExamen() { return idExamen; }
        public void setIdExamen(Integer idExamen) { this.idExamen = idExamen; }

        public SeccionEntity getSeccion() { return seccion; }
        public void setSeccion(SeccionEntity seccion) { this.seccion = seccion; }

        public List<PreguntaExamenEntity> getPreguntas() { return preguntas; }
        public void setPreguntas(List<PreguntaExamenEntity> preguntas) { this.preguntas = preguntas; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public Integer getPuntosAumento() { return puntosAumento; }
        public void setPuntosAumento(Integer puntosAumento) { this.puntosAumento = puntosAumento; }
    }
