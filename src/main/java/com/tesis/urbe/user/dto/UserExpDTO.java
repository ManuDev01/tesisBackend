package com.tesis.urbe.user.dto;

/**
 * EXP de un usuario devuelta por {@code GET /user/expByUser}: id, nombre y
 * experiencia total (suma de las cinco tablas de progreso).
 */
public record UserExpDTO(
        Integer idUsuario,
        String nombreUsuario,
        Integer totalExp
) {}
