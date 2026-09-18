package com.tesis.urbe.auth.dto;

public record ResetPasswordDTO(
        String correo,
        String codigo,
        String nuevaContrasena
) {
}
