package com.common.security.service;

import org.springframework.stereotype.Service;

@Service
public interface JwtTokenService {

    boolean validateToken(String token);

}