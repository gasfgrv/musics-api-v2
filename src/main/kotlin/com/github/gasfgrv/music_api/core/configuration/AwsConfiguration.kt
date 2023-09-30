package com.github.gasfgrv.music_api.core.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient

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
  @Profile("local")
  fun devAwsCredentialsProvider(): AwsCredentialsProvider {
    logger.info("Getting the AWS Account Credential Provider")
    val credentials = AwsBasicCredentials.create(accessKey, secretKey)
    return StaticCredentialsProvider.create(credentials);
  }

  @Bean
  @Profile("ec2")
  fun awsCredentialsProvider(): AwsCredentialsProvider {
    logger.info("Getting the AWS Account Credential Provider")
    return InstanceProfileCredentialsProvider.builder()
      .profileName("api_profile")
      .build()
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
    logger.info("Getting the DynamoDB mapper")
    return DynamoDbEnhancedClient.builder()
      .dynamoDbClient(dynamoDbClient)
      .build()
  }

  @Bean
  fun secretsManagerClient(awsCredentialsProvider: AwsCredentialsProvider): SecretsManagerClient {
    logger.info("Getting the Secrets Manager Client")
    return SecretsManagerClient.builder()
      .region(Region.of(signingRegion))
      .credentialsProvider(awsCredentialsProvider)
      .build()
  }
}
