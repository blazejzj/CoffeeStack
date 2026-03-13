package org.blazejzj.coffeestack.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String generateToken(UUID id) {
        Date now = new Date();
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    public UUID extractUserId(String token) {
        String subject = Jwts.parser().
                verifyWith(getKey())
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return UUID.fromString(subject);
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

