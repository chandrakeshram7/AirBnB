package com.cantech.projects.airBnbApp.security;

import com.cantech.projects.airBnbApp.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    private String jwt_secret_key;

    SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwt_secret_key.getBytes(StandardCharsets.UTF_8));
    }

    public String getAccessToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 10))
                .signWith(getSecretKey())
                .compact();
    }

    public String getRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000L * 60 * 24 * 30 * 6))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token){
        return Long.valueOf(
                Jwts
                        .parser()
                        .verifyWith(getSecretKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }





}
