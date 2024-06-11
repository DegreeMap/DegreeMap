package com.degreemap.DegreeMap.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "Gv8bL2Ru2lerM+GgLi1nMIjV7k2kZahXlm/ALHUVmWI="; 
    // openssl rand -base64 32

    public static String generateToken(String username) {
        long expirationTime = (1000 * 60 * 60); // Token validity 1 hour
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
