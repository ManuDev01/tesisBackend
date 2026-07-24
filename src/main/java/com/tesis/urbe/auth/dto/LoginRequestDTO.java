package com.tesis.urbe.auth.dto;

import com.tesis.urbe.user.dto.UserDTO;

// Para recibir las credenciales
public record LoginRequestDTO(
        String nombreUsuario,
        String contrasena
) {}