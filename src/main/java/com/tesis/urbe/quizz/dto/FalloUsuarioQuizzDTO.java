package com.tesis.urbe.quizz.dto;

import com.tesis.urbe.quizz.entity.FalloUsuarioQuizzEntity;

public record FalloUsuarioQuizzDTO(
        Integer idFallaDeUsuarioEnQuizz,
        Integer idUsuario,
        Integer idQuizz
) {
    public static FalloUsuarioQuizzDTO fromEntity(FalloUsuarioQuizzEntity entity) {
        if (entity == null) return null;

        return new FalloUsuarioQuizzDTO(
                entity.getIdFallaDeUsuarioEnQuizz(),
                entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null,
                entity.getQuizz() != null ? entity.getQuizz().getIdQuizz() : null
        );
    }
}
