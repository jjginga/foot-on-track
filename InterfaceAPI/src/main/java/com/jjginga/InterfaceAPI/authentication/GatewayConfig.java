package com.jjginga.InterfaceAPI.authentication;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/v2/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**",
                                "/webjars/**", "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .httpBasic().disable();  // Disable basic authentication if not needed

        return http.build();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tracking-service", r -> r.path("/tracking-service/**")
                        .uri("http://localhost:8091"))
                .route("analysis-service", r -> r.path("/analysis-service/**")
                        .uri("http://localhost:8092"))
                .route("training-plan", r -> r.path("/training-plan/**")
                        .uri("http://localhost:8093"))
                .build();
    }
}

