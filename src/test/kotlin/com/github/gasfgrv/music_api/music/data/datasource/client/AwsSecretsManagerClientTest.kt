package com.github.gasfgrv.music_api.music.data.datasource.client

import com.github.gasfgrv.music_api.configuration.AbstractTestcontainersIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class AwsSecretsManagerClientTest : AbstractTestcontainersIntegrationTest() {
  @Autowired
  private lateinit var awsSecretsManagerClient: AwsSecretsManagerClient

  @Test
  fun getSecret() {
    val spotifyClientId = awsSecretsManagerClient.getSecret("spotify_client_id")
    assertThat(spotifyClientId).isNotBlank().hasSize(32)
    val spotifyClientSecret = awsSecretsManagerClient.getSecret("spotify_client_secret")
    assertThat(spotifyClientSecret).isNotBlank().hasSize(32)
  }
}