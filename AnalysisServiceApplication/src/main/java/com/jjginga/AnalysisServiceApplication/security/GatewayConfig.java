package com.jjginga.AnalysisServiceApplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtTokenService jwtTokenService;  // Garantindo que JwtTokenService seja injetado corretamente

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenService);  // Criando JwtAuthenticationFilter como um bean
    }

    @Bean
    public SecurityWebFilterChain gatewaySecurityWebFilterChain(ServerHttpSecurity http) {
        http.addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
