package com.tesis.urbe.proyectos.dto;

import java.util.List;

public record UsuarioProyectosRankingDTO(
        Integer idUsuario,
        String nombreUsuario,
        Long totalProyectosCompletados,
        List<ProyectoDetalleDTO> proyectos
) {}