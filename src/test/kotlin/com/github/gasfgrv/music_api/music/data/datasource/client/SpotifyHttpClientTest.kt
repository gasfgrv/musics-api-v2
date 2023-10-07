package com.github.gasfgrv.music_api.music.data.datasource.client

import com.github.gasfgrv.music_api.configuration.AbstractTestcontainersIntegrationTest
import com.github.gasfgrv.music_api.configuration.WireMockConfig
import com.github.gasfgrv.music_api.mocks.Mocks
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.util.ResourceUtils
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient

@Import(WireMockConfig::class)
class SpotifyHttpClientTest : AbstractTestcontainersIntegrationTest() {
  @Autowired
  private lateinit var wireMockServer: WireMockServer

  @Autowired
  private lateinit var spotifyHttpClient: SpotifyHttpClient

  @Autowired
  private lateinit var secretsManagerClient: SecretsManagerClient

  @BeforeEach
  fun setUp() {
    mockAccessToken()
    mockSearch()
    mockResponse()
  }

  @Test
  fun getTrackInfo(capturedOutput: CapturedOutput) {
    val musicMock = Mocks.music()
    val music = spotifyHttpClient.getTrackInfo(musicMock)
    assertThat(capturedOutput.out).contains("Returning song data")
    assertThat(music).isNotNull.isExactlyInstanceOf(musicMock::class.java)
    assertThat(music.id).isNull()
  }

  private fun mockAccessToken() {
    wireMockServer.stubFor(
      post(urlEqualTo("/api/token"))
        .withHeader("content-type", containing("x-www-form-urlencoded"))
        .withFormParam("grant_type", equalTo("client_credentials"))
        .withFormParam("client_id", matching("[\\D\\d]{32}"))
        .withFormParam("client_secret", matching("[\\D\\d]{32}"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(
              ResourceUtils.getFile("classpath:payload/spotify-token.json").readBytes()
            )
        )
    )
  }

  private fun mockSearch() {
    val queryParams = HashMap<String, StringValuePattern>()
    queryParams["q"] = containing("Casio")
    queryParams["type"] = containing("track")
    queryParams["market"] = containing("BR")
    queryParams["limit"] = containing("1")
    queryParams["offset"] = containing("0")
    queryParams["include_external"] = containing("audio")

    wireMockServer.stubFor(
      get(urlPathMatching("/v1/search.*"))
        .withQueryParams(queryParams)
        .withHeader("authorization", matching("^(Bearer)\\s.*\$"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(
              ResourceUtils.getFile("classpath:payload/search-response.json").readBytes()
            )
        )
    )
  }

  private fun mockResponse() {
    wireMockServer.stubFor(
      get(urlPathMatching("/v1/tracks/[\\d\\D]{22}.*"))
        .withQueryParam("market", equalTo("BR"))
        .withHeader("authorization", matching("^(Bearer)\\s.*\$"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(
              ResourceUtils.getFile("classpath:payload/track-response.json").readBytes()
            )
        )
    )
  }
}
