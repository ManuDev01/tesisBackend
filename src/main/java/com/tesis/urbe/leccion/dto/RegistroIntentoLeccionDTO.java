package com.tesis.urbe.leccion.dto;

/**
 * Intento de lección registrado, devuelto por
 * {@code POST /leccion/registrarIntentoLeccion/{idLeccion}/user/{idUsuario}}.
 */
public record RegistroIntentoLeccionDTO(
        Integer idIntentoLeccion,
        Integer idUsuario,
        Integer idLeccion
) {}
