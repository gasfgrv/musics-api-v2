package com.github.gasfgrv.music_api.music.data.secrets

import org.springframework.stereotype.Component
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse


@Component
class AwsSecretsManagerClient(private val secretsManagerClient: SecretsManagerClient) {
  fun getSecret(secretName: String): String? {
    val valueRequest = GetSecretValueRequest.builder()
      .secretId(secretName)
      .build()
    val valueResponse: GetSecretValueResponse = secretsManagerClient.getSecretValue(valueRequest)
    return valueResponse.secretString()
  }
}