package com.github.gasfgrv.music_api.utils

import java.net.URI
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest

class LocalstackUtils {
  companion object {
    fun deployServices(containerInfo: LocalStackContainer) {
      createSecrets(getSecretsManager(containerInfo))
      createTable(getDynamoDB(containerInfo))
    }

    fun destroyServices(containerInfo: LocalStackContainer) {
      destroySecrets(getSecretsManager(containerInfo))
      destroyTable(getDynamoDB(containerInfo))
    }

    private fun getSecretsManager(containerInfo: LocalStackContainer): SecretsManagerClient {
      return SecretsManagerClient.builder()
        .region(mockRegion(containerInfo.region))
        .endpointOverride(mockEndpoint(containerInfo, SECRETSMANAGER))
        .credentialsProvider(mockCredentials(containerInfo.accessKey, containerInfo.secretKey))
        .build()
    }

    private fun getDynamoDB(containerInfo: LocalStackContainer): DynamoDbClient {
      return DynamoDbClient.builder()
        .region(mockRegion(containerInfo.region))
        .endpointOverride(mockEndpoint(containerInfo, DYNAMODB))
        .credentialsProvider(mockCredentials(containerInfo.accessKey, containerInfo.secretKey))
        .build()
    }

    private fun mockCredentials(accessKey: String, secretKey: String): StaticCredentialsProvider {
      return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
    }

    private fun mockEndpoint(containerInfo: LocalStackContainer, service: LocalStackContainer.Service): URI {
      return containerInfo.getEndpointOverride(service)
    }

    private fun mockRegion(region: String): Region {
      return Region.of(region)
    }

    private fun createSecrets(secretsManagerClient: SecretsManagerClient) {
      secretsManagerClient.createSecret(
        CreateSecretRequest.builder()
          .name("spotify_client_id")
          .secretString("gd2h8r2e882aj08oz56c9vkbpjdduutk")
          .build()
      )
      secretsManagerClient.createSecret(
        CreateSecretRequest.builder()
          .name("spotify_client_secret")
          .secretString("gd2h8r2e882aj08oz56c9vkbpjdduutk")
          .build()
      )
    }

    private fun destroySecrets(secretsManagerClient: SecretsManagerClient) {
      secretsManagerClient.deleteSecret(
        DeleteSecretRequest.builder()
          .secretId("spotify_client_id")
          .forceDeleteWithoutRecovery(true)
          .build()
      )
      secretsManagerClient.deleteSecret(
        DeleteSecretRequest.builder()
          .secretId("spotify_client_secret")
          .forceDeleteWithoutRecovery(true)
          .build()
      )
    }

    private fun createTable(dynamoDbClient: DynamoDbClient) {
      val keySchemaElements = mutableListOf(
        KeySchemaElement.builder().attributeName("MusicId").keyType(KeyType.HASH).build(),
        KeySchemaElement.builder().attributeName("MusicName").keyType(KeyType.RANGE).build()
      )
      val attributeDefinitions = mutableListOf(
        AttributeDefinition.builder().attributeName("MusicId").attributeType(ScalarAttributeType.S).build(),
        AttributeDefinition.builder().attributeName("MusicName").attributeType(ScalarAttributeType.S).build()
      )
      dynamoDbClient.createTable(
        CreateTableRequest.builder()
          .tableName("MusicsTb")
          .keySchema(keySchemaElements)
          .attributeDefinitions(attributeDefinitions)
          .billingMode(BillingMode.PAY_PER_REQUEST)
          .build()
      )
    }

    private fun destroyTable(dynamoDbClient: DynamoDbClient) {
      dynamoDbClient.deleteTable(
        DeleteTableRequest.builder()
          .tableName("MusicsTb")
          .build()
      )
    }
  }
}