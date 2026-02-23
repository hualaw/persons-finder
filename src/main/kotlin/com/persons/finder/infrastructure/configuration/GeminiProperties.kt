package com.persons.finder.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "gemini.api")
data class GeminiProperties(
    val key: String,
    val timeout: Duration
)
