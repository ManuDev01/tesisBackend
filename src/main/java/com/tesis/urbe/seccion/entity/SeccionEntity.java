package com.tesis.urbe.seccion.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

@Entity

public class SeccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seccion")
    private Integer idSeccion;

    public SeccionEntity() {

    }
}
