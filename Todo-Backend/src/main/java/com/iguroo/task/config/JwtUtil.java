
package com.iguroo.task.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")  // ✅ Read from application.properties
    private String secretKey;

    // ✅ Get signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Extract Username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract Expiration Date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ Extract Specific Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Extract All Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Check if Token is Expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Generate Access Token with Role
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username, 1000 * 60 * 60 * 10); // 10 hours
    }

    // ✅ Generate Refresh Token
    public String generateRefreshToken(String username) {
        return createToken(new HashMap<>(), username, 1000 * 60 * 60 * 24 * 7); // 7 days
    }

    // ✅ Token Creation Logic
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Validate Token
    public Boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
}
