package edu.icet.ecom.service.impl;

import edu.icet.ecom.enums.UserRole;
import edu.icet.ecom.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Getter
@Setter
public class JwtServiceImpl implements JwtService {

    private final long EXPIRATION_TIME;
    private final String SECRET;
    private final Key key;

    public JwtServiceImpl(
            @Value("${jwt.expiration}") long expirationTime,
            @Value("${jwt.secret}") String secret) {
        this.EXPIRATION_TIME = expirationTime;
        this.SECRET = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(String email, UserRole role, String clientId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("client_id", clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + setExpirationTime(clientId)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public long setExpirationTime(String clientId) {
        return "MOBILE".equals(clientId) ? EXPIRATION_TIME * 30 : EXPIRATION_TIME;
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


}

