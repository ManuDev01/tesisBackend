package com.tesis.urbe.user.dto;

public record UserDTO(
        Integer idUsuario,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String nombreUsuario,
        String cedula,
        String correo,
        String contrasena, // Lo agregamos aquí para recibirlo en el POST
        Integer idRol,
        boolean activo
) {
    public static UserDTO fromEntity(com.tesis.urbe.user.entity.UserEntity entity) {
        return new UserDTO(
                entity.getIdUsuario(),
                entity.getPrimerNombre(),
                entity.getSegundoNombre(),
                entity.getPrimerApellido(),
                entity.getSegundoApellido(),
                entity.getNombreUsuario(),
                entity.getCedula(),
                entity.getCorreo(),
                null, // No enviamos la contraseña en las consultas (GET)
                entity.getIdRol() != null ? entity.getIdRol().getIdRol() : null,
                entity.isActivo()
        );
    }
}