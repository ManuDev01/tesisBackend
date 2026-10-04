package com.tesis.urbe.medallas.dto;

public record UsuarioMedallaCountDTO(
        Integer idUsuario,
        String nombreUsuario, // O el campo que uses para el nombre/alias
        Long totalMedallas
) {}