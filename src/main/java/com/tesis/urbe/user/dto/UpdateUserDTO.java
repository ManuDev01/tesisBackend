package com.tesis.urbe.user.dto;

public record UpdateUserDTO(
        Integer idUsuario,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String nombreUsuario,
        String cedula,
        String correo
) {
    public static UpdateUserDTO fromEntity(com.tesis.urbe.user.entity.UserEntity entity) {
        return new UpdateUserDTO(
                entity.getIdUsuario(),
                entity.getPrimerNombre(),
                entity.getSegundoNombre(),
                entity.getPrimerApellido(),
                entity.getSegundoApellido(),
                entity.getNombreUsuario(),
                entity.getCedula(),
                entity.getCorreo()
        );
    }
}
