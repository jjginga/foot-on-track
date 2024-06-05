package com.jjginga.AnalysisServiceApplication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public GroupedOpenApi publicApi() {
        logger.info("Initializing Swagger Public API Group");
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Initializing Swagger Custom OpenAPI");
        return new OpenAPI()
                .info(new Info()
                        .title("Analysis Service API")
                        .version("1.0")
                        .description("API documentation for Analysis Service"));
    }

}
