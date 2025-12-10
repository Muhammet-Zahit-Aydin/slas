package com.example.slas.service ;

import io.jsonwebtoken.Jwts ;
import io.jsonwebtoken.SignatureAlgorithm ;
import io.jsonwebtoken.io.Decoders ;
import io.jsonwebtoken.security.Keys ;
import org.springframework.stereotype.Service ;

import java.security.Key ;
import java.util.Date ;
import java.util.HashMap ;
import java.util.Map ;

@Service
public class JwtService {

    // Secret key for signing the JWT
    private static final String SECRET_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437" ;

    public String generateToken (String userName) {

        Map<String, Object> claims = new HashMap<>() ;
        return createToken(claims, userName) ;

    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName) // Owner of the token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours validation
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Signing the token
                .compact() ;
    }

    private Key getSignKey() {
        
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY) ;
        return Keys.hmacShaKeyFor(keyBytes) ;

    }

}