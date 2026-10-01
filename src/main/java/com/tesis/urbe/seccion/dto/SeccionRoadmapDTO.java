package com.tesis.urbe.seccion.dto;

import java.util.List;

public record SeccionRoadmapDTO(
        Integer idSeccion,
        String titulo,
        Integer orden,
        List<NodoCaminoDTO> nodos
) {}