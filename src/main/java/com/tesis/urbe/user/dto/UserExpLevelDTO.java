package com.tesis.urbe.user.dto;

/**
 * EXP de un usuario en un nivel/curso, devuelta por
 * {@code GET /user/expByLevel/{idUsuario}}.
 *
 * <p>Un "nivel" es un registro de la tabla {@code curso} (Básico, Intermedio,
 * Avanzado); las secciones se vinculan al curso con {@code seccion.idcurso}.
 */
public record UserExpLevelDTO(
        Integer idCurso,
        String nombreCurso,
        Integer totalExp
) {}
