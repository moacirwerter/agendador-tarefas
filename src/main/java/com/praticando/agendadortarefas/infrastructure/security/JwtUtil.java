
package com.praticando.agendadortarefas.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    // Chave secreta carregada do application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Converte a chave secreta para validação JWT
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Extrai e valida as informações do token JWT
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrai o email do usuário armazenado no token
    public String extrairEmailToken(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();

        return expiration == null || !expiration.after(new Date());
    }

    // Valida o token, o usuário e o prazo de expiração
    public boolean validateToken(String token, String username) {
        Claims claims = extractClaims(token);

        String extractedUsername = claims.getSubject();
        Date expiration = claims.getExpiration();

        return extractedUsername != null
                && extractedUsername.equals(username)
                && expiration != null
                && expiration.after(new Date());
    }
}
