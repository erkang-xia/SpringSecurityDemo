package com.example.springsecuritydemo.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
    String generateToken(String username);

    String extractUsername(String jwtToken);

    boolean validateToken(String jwtToken);

    Collection<? extends GrantedAuthority> getAuthorities(String jwtToken);
}
