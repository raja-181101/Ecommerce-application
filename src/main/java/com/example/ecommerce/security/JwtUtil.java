package com.example.ecommerce.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key SECRETE_KEY = Keys.hmacShaKeyFor("181101161144211811011611442118110116114421".getBytes());
    //private final String SECRETE_KEY = "18110116114421";

    public String generateToken(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SECRETE_KEY)
                .compact();

    }

    public String getUserName(String token){
        return Jwts.parser()
                .setSigningKey(SECRETE_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
