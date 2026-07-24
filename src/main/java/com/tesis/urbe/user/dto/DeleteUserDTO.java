package com.tesis.urbe.user.dto;

public record DeleteUserDTO(
        Integer idUsuario

) {
    public static DeleteUserDTO fromEntity(com.tesis.urbe.user.entity.UserEntity entity) {
        return new DeleteUserDTO(
                entity.getIdUsuario()
        );
    }
}
