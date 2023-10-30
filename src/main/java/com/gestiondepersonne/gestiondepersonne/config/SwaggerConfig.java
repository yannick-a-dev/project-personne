package com.gestiondepersonne.gestiondepersonne.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomiser customOpenAPI() {
        return openApi -> openApi.info(new Info()
            .title("Gestion de Personne API")
            .version("1.0")
            .description("API for managing personne details")
        );
    }
}




