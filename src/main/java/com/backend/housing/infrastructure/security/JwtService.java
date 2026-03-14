package com.backend.housing.infrastructure.security;

import java.util.Date;

import javax.crypto.SecretKey; // IMPORTANTE: Usar SecretKey

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    private final String SECRET = "ESTA_ES_UNA_LLAVE_MUY_SECRET_Y_LARGA_DE_32_CHARS";

    private SecretKey getSingKey() { 
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
            .signWith(getSingKey())
            .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(getSingKey()) 
            .build()
            .parseSignedClaims(token)
            .getPayload() 
            .getSubject();
    }
}