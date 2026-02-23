package com.persons.finder

import com.persons.finder.infrastructure.configuration.GeminiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(GeminiProperties::class)
class ApplicationStarter

fun main(args: Array<String>) {
	runApplication<ApplicationStarter>(*args)
}
