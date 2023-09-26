package com.github.gasfgrv.music_api.core.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAPIConfig {
    private val logger = LoggerFactory.getLogger(OpenAPIConfig::class.java)

    @Bean
    fun openAPI(): OpenAPI {
        logger.info("Setting the API server info")
        val server = Server()
        server.url = "http://localhost:8080"
        server.description = "Server URL in the development environment"

        logger.info("Setting the API developer contact")
        val contact = Contact()
        contact.email = "gustavo_almeida11@hotmail.com"
        contact.name = "Gustavo Silva"
        contact.url = "https://github.com/gasfgrv"

        logger.info("Setting the API info")
        val info = Info()
            .title("Music API")
            .version("1.0")
            .contact(contact)
            .description("This API serves as a test for the AWS SDK for DynamoDB functions (query, scan, load and save)")

        logger.info("Getting Open API Schema")
        return OpenAPI()
            .info(info)
            .servers(listOf(server))
    }
}
