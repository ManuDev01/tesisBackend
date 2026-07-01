package com.tesis.urbe.auth.dto;

import com.tesis.urbe.user.dto.UserDTO;

// Para responder con el token
public record LoginResponseDTO(
        String token,
        UserDTO usuario
) {}