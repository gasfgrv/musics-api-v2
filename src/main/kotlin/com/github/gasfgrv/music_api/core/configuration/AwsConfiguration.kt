package com.github.gasfgrv.music_api.core.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Configuration
class AwsConfiguration {
  private val logger = LoggerFactory.getLogger(AwsConfiguration::class.java)

  @Value("\${aws.signingRegion}")
  private lateinit var signingRegion: String

  @Value("\${aws.accessKey}")
  private lateinit var accessKey: String

  @Value("\${aws.secretKey}")
  private lateinit var secretKey: String

  @Bean
  fun awsCredentials(): AwsCredentials {
    logger.info("Getting AWS account credentials")
    return AwsBasicCredentials.create(accessKey, secretKey)
  }

  @Bean
  fun awsCredentialsProvider(awsCredentials: AwsCredentials): AwsCredentialsProvider {
    logger.info("Getting the AWS Account Credential Provider")
    return StaticCredentialsProvider.create(awsCredentials)
  }

  @Bean
  fun dynamoDbClient(awsCredentialsProvider: AwsCredentialsProvider): DynamoDbClient {
    logger.info("Getting the DynamoDB Client")
    return DynamoDbClient.builder()
      .region(Region.of(signingRegion))
      .credentialsProvider(awsCredentialsProvider)
      .build()
  }

  @Bean
  fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
    logger.info("")
    return DynamoDbEnhancedClient.builder()
      .dynamoDbClient(dynamoDbClient)
      .build()
  }
}
