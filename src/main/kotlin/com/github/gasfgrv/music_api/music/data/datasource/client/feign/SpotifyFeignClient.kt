package com.github.gasfgrv.music_api.music.data.datasource.client.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "spotifyFeignClient", url = "\${spotify.apiUrl}")
interface SpotifyFeignClient {
  @GetMapping(value = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun searchTrack(
    @RequestParam("q") query: String,
    @RequestParam("type") type: String,
    @RequestParam("market") market: String,
    @RequestParam("limit") limit: String,
    @RequestParam("offset") offset: String,
    @RequestParam("include_external") includeExternal: String,
    @RequestHeader("authorization") accessToken: String
  ): ResponseEntity<String>

  @GetMapping(value = ["/tracks/{trackId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getTrack(
    @PathVariable("trackId") id: String,
    @RequestParam("market") market: String,
    @RequestHeader("authorization") accessToken: String
  ): ResponseEntity<String>
}