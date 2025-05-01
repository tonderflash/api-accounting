package com.pluralsight.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger simplificada.
 * La documentación de API se ha eliminado temporalmente para facilitar el desarrollo inicial.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ultra Accounting API")
                        .version("1.0.0")
                        .description("REST API documentation for Ultra Accounting Ledger"));
    }
}
