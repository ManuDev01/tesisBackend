package com.tesis.urbe.medallas.dto;

import java.util.Date;

public record MedallaDTO(
        Integer idMedalla,
        String nombreMedalla,
        String descripcionMedalla,
        Integer valorMedalla,
        Date createAt
) {
}
