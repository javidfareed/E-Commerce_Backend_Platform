package com.scaler.userauthservice.service;

import com.scaler.userauthservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    //Jwt service needs key and expiry time

    private final SecretKey key;
    private final long expirySeconds;

    public JwtService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiry-seconds}") long expirySeconds)
    {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirySeconds = expirySeconds;
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .issuer("userAuthService")
                .expiration(new Date(System.currentTimeMillis() + expirySeconds*1000))
                .claim("userId", String.valueOf(user.getId()))
                .claim("role",user.getRole().name()) //converting role to String
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {

         return Jwts.parser()
                 .verifyWith(key)
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
    }
}
