package edu.icet.ecom.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

     String generateToken(String email, String role, String clientId) ;

     Claims extractClaims(String token) ;

     String extractEmail(String token);

     String extractRole(String token);

     boolean isTokenValid(String token) ;
}

