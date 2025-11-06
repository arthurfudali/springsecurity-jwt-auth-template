package com.dev.arthur.springsecurity_jwt_auth.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dev.arthur.springsecurity_jwt_auth.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final Algorithm algorithm;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("spring-auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating JWT token", e);
        }
    }

    public String extractSubject(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("spring-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
