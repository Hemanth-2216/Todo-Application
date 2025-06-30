
package com.spboot.todo.config;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Add this import

@Component
public class JwtUtil {

@Value("${jwt.secret}")
private String SECRET_KEY;

public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
}

public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractClaims(token);
    return claimsResolver.apply(claims);
}

public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseClaimsJws(token)
        .getBody();
}

public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    claims.put("roles", roles);
    return createToken(claims, userDetails.getUsername(), 1000 * 60 * 60 * 10); // 10 hours
}

public String generateRefreshToken(UserDetails userDetails) {
    return createToken(new HashMap<>(), userDetails.getUsername(), 1000 * 60 * 60 * 24 * 7); // 7 days
}

private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
        .compact();
}

public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}

private Boolean isTokenExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
}

// This method will be used in JwtAuthenticationFilter
public UsernamePasswordAuthenticationToken getAuthentication(String token, UserDetails userDetails) {
    Claims claims = extractClaims(token);
    List<String> roles = claims.get("roles", List.class);
    List<GrantedAuthority> authorities = roles != null ?
        roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()) :
        Collections.emptyList();

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
}
}
