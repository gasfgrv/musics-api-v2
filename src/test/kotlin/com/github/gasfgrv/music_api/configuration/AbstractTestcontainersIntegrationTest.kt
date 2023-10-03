package com.github.gasfgrv.music_api.configuration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTestcontainersIntegrationTest {
  companion object Containter {
    @Container
    private val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
      .apply {
        withServices(LocalStackContainer.Service.DYNAMODB)
        withReuse(true)
      }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      registry.add("aws.signingRegion") { localStackContainer.region }
      registry.add("aws.accessKey") { localStackContainer.accessKey }
      registry.add("aws.secretKey") { localStackContainer.secretKey }
    }

    @JvmStatic
    @BeforeAll
    internal fun setUp() {
      localStackContainer.start()
    }

    @JvmStatic
    @AfterAll
    fun tearDown() {
      localStackContainer.stop()
    }
  }
}