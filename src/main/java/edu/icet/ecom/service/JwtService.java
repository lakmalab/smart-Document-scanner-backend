package edu.icet.ecom.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface JwtService {

    String generateToken(String username, String role) ;

     String extractUsername(String token);

     String extractRole(String token);

}

