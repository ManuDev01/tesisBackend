package com.tesis.urbe.user.entity;

import com.tesis.urbe.rol.entity.RolEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(name = "primernombre")
    private String primerNombre;

    @Column(name = "segundonombre")
    private String segundoNombre;

    @Column(name = "primerapellido")
    private String primerApellido;

    @Column(name = "segundoapellido")
    private String segundoApellido;

    @Column(name = "nombreusuario")
    private String nombreUsuario;

    @Column(name = "cedula")
    private String cedula;

    @Column(name = "correo")
    private String correo;

    @Column(name = "contrasena")
    private String contrasena;

    @OneToOne
    @JoinColumn(name = "idrol", referencedColumnName = "idrol")
    private RolEntity idRol;

    @Column(name = "activo")
    private boolean activo;

    public UserEntity(Integer idUsuario, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String nombreUsuario, String cedula, String correo, String contrasena, RolEntity idRol, boolean activo) {
        this.idUsuario = idUsuario;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.nombreUsuario = nombreUsuario;
        this.cedula = cedula;
        this.correo = correo;
        this.contrasena = contrasena;
        this.idRol = idRol;
        this.activo = activo;
    }

    public UserEntity() {

    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public RolEntity getIdRol() {
        return idRol;
    }

    public void setIdRol(RolEntity idRol) {
        this.idRol = idRol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
