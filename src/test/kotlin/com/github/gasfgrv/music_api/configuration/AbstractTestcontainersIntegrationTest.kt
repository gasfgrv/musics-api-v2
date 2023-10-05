package com.github.gasfgrv.music_api.configuration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.BillingMode.PAY_PER_REQUEST
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType.HASH
import software.amazon.awssdk.services.dynamodb.model.KeyType.RANGE
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType.S

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTestcontainersIntegrationTest {
  @Autowired
  private lateinit var dynamoDbClient: DynamoDbClient

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
    }

    @JvmStatic
    @BeforeAll
    internal fun beforeAll() {
      localStackContainer.start()
    }

    @JvmStatic
    @AfterAll
    fun afterAll() {
      localStackContainer.stop()
    }
  }

  @BeforeEach
  fun beforeEach() {
    val keySchemaElements = mutableListOf(
      KeySchemaElement.builder().attributeName("MusicId").keyType(HASH).build(),
      KeySchemaElement.builder().attributeName("MusicName").keyType(RANGE).build()
    )
    val attributeDefinitions = mutableListOf(
      AttributeDefinition.builder().attributeName("MusicId").attributeType(S).build(),
      AttributeDefinition.builder().attributeName("MusicName").attributeType(S).build()
    )
    val createTableRequest = CreateTableRequest.builder()
      .tableName("MusicsTb")
      .keySchema(keySchemaElements)
      .attributeDefinitions(attributeDefinitions)
      .billingMode(PAY_PER_REQUEST)
      .build()
    dynamoDbClient.createTable(createTableRequest)
  }

  @AfterEach
  fun afterEach() {
    val deleteTableRequest = DeleteTableRequest.builder().tableName("MusicsTb").build()
    dynamoDbClient.deleteTable(deleteTableRequest)
  }
}
