package com.tesis.urbe.user.dto;

public record UserRankingDTO(
        Integer idUsuario,
        String nombreUsuario,
        String primerNombre,
        String primerApellido,
        Integer totalExp
) {}