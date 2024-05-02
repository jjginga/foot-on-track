package com.jjginga.AnalysisServiceApplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()  // Assegura que o Actuator não requer autenticação
                        .anyExchange().authenticated())  // Assegura que todas as outras requisições sejam autenticadas
                .httpBasic().disable()  // Desabilita a autenticação básica HTTP se necessário
                .formLogin().disable();  // Desabilita o formulário de login
        return http.build();
    }
}
