package com.app.coursecenter.service.authenticationservice;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(Map<String, Object> claims, UserDetails userDetails);
}
