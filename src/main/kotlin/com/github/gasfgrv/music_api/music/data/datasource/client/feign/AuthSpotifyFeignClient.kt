package com.github.gasfgrv.music_api.music.data.datasource.client.feign

import com.github.gasfgrv.music_api.core.configuration.FormFeignEncoderConfig
import com.github.gasfgrv.music_api.music.data.model.dto.AccessTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
  name = "authSpotifyFeignClient",
  url = "\${spotify.authUrl}",
  configuration = [FormFeignEncoderConfig::class]
)
fun interface AuthSpotifyFeignClient {
  @PostMapping(
    value = ["/api/token"],
    consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getAccessToken(@RequestBody request: Map<String, Any>): AccessTokenResponse
}