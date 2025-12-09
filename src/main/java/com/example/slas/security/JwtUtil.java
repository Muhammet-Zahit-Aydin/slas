package com.example.slas.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "THIS_IS_A_VERY_SECRET_KEY_32_CHAR_LONG!!"; // Min 32 chars

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Valid for 1 day
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact() ;

    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject() ;

    }

    public boolean isValid(String token) {

        try {
            extractUsername(token) ;
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}