package com.tesis.urbe.user.model;


import jakarta.persistence.*;
import com.tesis.urbe.user.UserGenero;

@Entity(name = "User")
@Table(name = "user", schema = "public")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String primerNombre;

    private String segundoNombre;

    @Column(nullable = false)
    private String primerApellido;

    private String segundoApellido;

    @Column(nullable = false, unique = true)
    private String cedula;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private UserGenero genero;

    private Integer idInstituto;

    private Integer idRol;
}
