package com.persons.finder.infrastructure.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Persons Finder API")
                    .version("1.0.0")
                    .description("Backend API for the Persons Finder application (AI-Augmented Edition).")
            )
    }
}
