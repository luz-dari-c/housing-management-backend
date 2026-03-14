package com.backend.housing.infrastructure.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthentificationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        String header = request.getHeader("Autorization");

        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            String email = jwtService.extractEmail(token);
            request.setAttribute("userEmail", email );

        } catch (Exception e) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return;
        }

        filterChain.doFilter(request, response);
    }

    
}
