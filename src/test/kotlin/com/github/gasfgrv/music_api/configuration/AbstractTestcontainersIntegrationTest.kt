package com.github.gasfgrv.music_api.configuration

import com.github.gasfgrv.music_api.utils.LocalstackUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension::class)
abstract class AbstractTestcontainersIntegrationTest {
  companion object Containter {
    @JvmStatic
    @Container
    protected val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
      .apply {
        withServices(DYNAMODB, SECRETSMANAGER)
        withReuse(true)
      }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      registry.add("aws.serviceEndpoint.dynamo") { localStackContainer.getEndpointOverride(DYNAMODB) }
      registry.add("aws.serviceEndpoint.secretsManager") { localStackContainer.getEndpointOverride(SECRETSMANAGER) }
      registry.add("aws.signingRegion") { localStackContainer.region }
      registry.add("aws.accessKey") { localStackContainer.accessKey }
      registry.add("aws.secretKey") { localStackContainer.secretKey }
      registry.add("spotify.authUrl") { "http://localhost:9561" }
      registry.add("spotify.apiUrl") { "http://localhost:9561/v1" }
    }

    @JvmStatic
    @BeforeAll
    fun beforeAll() {
      localStackContainer.start()
      LocalstackUtils.deployServices(localStackContainer)
    }

    @JvmStatic
    @AfterAll
    fun afterAll() {
      LocalstackUtils.destroyServices(localStackContainer)
      localStackContainer.stop()
    }
  }
}
