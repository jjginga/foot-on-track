package com.jjginga.InterfaceAPI.authentication;

import com.jjginga.InterfaceAPI.security.JwtAuthenticationFilter;
import com.jjginga.InterfaceAPI.security.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        return new JwtAuthenticationFilter(jwtTokenService);
    }
}
