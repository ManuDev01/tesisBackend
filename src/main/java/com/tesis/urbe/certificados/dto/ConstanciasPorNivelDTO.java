package com.tesis.urbe.certificados.dto;

import java.util.List;

public record ConstanciasPorNivelDTO(
        String nivel,
        Long totalUsuarios,
        List<UsuarioConstanciaDTO> usuarios
) {
    public record UsuarioConstanciaDTO(
            Integer idUsuario,
            String nombreCompleto
    ) {}
}