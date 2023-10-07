package com.github.gasfgrv.music_api.configuration

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class WireMockConfig {
  @Bean(initMethod = "start", destroyMethod = "stop")
  fun wireMockServer(): WireMockServer {
    return WireMockServer(9561)
  }
}