package com.example.ecommerce.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRETE_KEY = Keys.hmacShaKeyFor("181101161144211811011611442118110116114421".getBytes());

    public String generateToken(String userName, String role){
        return Jwts.builder()
                .setSubject(userName)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SECRETE_KEY)
                .compact();

    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith( SECRETE_KEY)
                .build().parseSignedClaims(token).getPayload();
    }

    public String getUserName(String token){
        return extractAllClaims(token).getSubject();
    }
    public String extractRole(String token){
        return extractAllClaims(token).get("role",String.class);
    }
}
