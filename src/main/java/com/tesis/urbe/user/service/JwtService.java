package com.tesis.urbe.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.tesis.urbe.user.entity.UserEntity;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String SECRET_KEY = "mi_clave_secreta_super_segura_para_la_tesis_urbe_2026";

    public String generateToken(UserEntity user) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // Usamos .put() en lugar de .add()
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("correo", user.getCorreo());
        extraClaims.put("rol", user.getIdRol() != null ? user.getIdRol().getIdRol() : null);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getNombreUsuario())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(key)
                .compact();
    }
}