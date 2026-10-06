package com.tesis.urbe.leccion.dto;

import java.util.List;

public record TasaErrorUsuarioDTO(
        Integer idUsuario,
        String usuario,
        Long aciertos,
        Long fallos,
        Double tasaError,
        Long fallosAntesDeResolver,
        List<ErrorLeccionDetalleDTO> erroresPorLeccion
) {}