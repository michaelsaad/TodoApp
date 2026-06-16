package com.m1kellz.todoservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI todoServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TodoApp Todo Service API")
                        .description("""
                                Todo CRUD protected by JWT.
                                Get a token from user-service (login), click Authorize, then call authenticated endpoints.
                                Token is validated via user-service through Eureka service discovery.""")
                        .version("1.0")
                        .contact(new Contact().name("TodoApp").email("support@todoapp.local")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT from user-service login. Format: Bearer <token>")));
    }
}
