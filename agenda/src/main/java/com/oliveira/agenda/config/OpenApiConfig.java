package com.oliveira.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    public OpenApiConfig() {
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return (new OpenAPI())
                .info((new Info())
                        .title("RESTFULL API with Java 17 and Spring Boot 3")
                        .version("v1")
                        .description("Contact book")
                        .termsOfService("http://oliveira.com.br/agenda")
                        .license((new License()).name("Apache 2.0")
                                .url("http://pub.oliveira.com.br/minhaaplicacao")));
    }
}