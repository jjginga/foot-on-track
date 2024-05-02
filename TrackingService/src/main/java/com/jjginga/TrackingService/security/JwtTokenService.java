package com.jjginga.TrackingService.security;

import org.springframework.security.core.Authentication;

public interface JwtTokenService {
    boolean validateToken(String token);
    String generateToken(Authentication authentication);
}