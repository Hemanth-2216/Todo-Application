
package com.spboot.todo.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token expired: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            } catch (MalformedJwtException | UnsupportedJwtException | SecurityException e) {
                logger.warn("Invalid JWT token: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            } catch (IllegalArgumentException e) {
                logger.warn("JWT claims string is empty: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Empty token");
                return;
            }
        } else {
            logger.info("No JWT token found in request header.");
        }

        // Validate and set security context if token is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(token, userDetails));
            } else {
                logger.warn("JWT validation failed for user: {}", username);
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
