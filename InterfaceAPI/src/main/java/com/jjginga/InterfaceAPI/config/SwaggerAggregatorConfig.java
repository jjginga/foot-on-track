package com.jjginga.InterfaceAPI.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Configuration
public class SwaggerAggregatorConfig implements WebFluxConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SwaggerAggregatorConfig.class);

    @Bean
    public GroupedOpenApi trackingServiceApi() {
        return GroupedOpenApi.builder()
                .group("TrackingService")
                .pathsToMatch("/tracking-service/**")
                .build();
    }

    @Bean
    public GroupedOpenApi analysisServiceApi() {
        return GroupedOpenApi.builder()
                .group("AnalysisService")
                .pathsToMatch("/analysis-service/**")
                .build();
    }

    @Bean
    public GroupedOpenApi trainingPlanApi() {
        return GroupedOpenApi.builder()
                .group("TrainingPlan")
                .pathsToMatch("/training-plan/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth-service/**")
                .build();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Configuring resource handlers for Swagger");
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service", r -> r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("tracking-service", r -> r.path("/tracking/**")
                        .uri("lb://TRACKING-SERVICE"))
                .route("analysis-service", r -> r.path("/analysis/**")
                        .uri("lb://ANALYSIS-SERVICE"))
                .route("planning-service", r -> r.path("/planning/**")
                        .uri("lb://PLANNING-SERVICE"))
                .route("swagger-ui", r -> r.path("/swagger-ui.html", "/webjars/**", "/v3/api-docs/**")
                        .uri("lb://YOUR-SERVICE-NAME"))  // Substitua "YOUR-SERVICE-NAME" pelo nome do seu serviço
                .build();
    }
}
