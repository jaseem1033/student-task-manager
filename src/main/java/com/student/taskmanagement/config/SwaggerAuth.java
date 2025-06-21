package com.student.taskmanagement.config;

import io.swagger.v3.oas.models.security.SecurityRequirement; // <--- THIS IS CORRECT
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAuth {
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // This is an arbitrary name for your security scheme

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP) // This means it's HTTP authentication
                                .scheme("bearer") // The scheme is "bearer" (e.g., Bearer <token>)
                                .bearerFormat("JWT"))); // The format of the bearer token is JWT
    }
}
