package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyResponse(
  val name: String,
  val href: String,
  val trackNumber: Int,
  val durationMs: Int,
  val explicit: Boolean,
  val popularity: Int,
  val album: Album,
  val artists: List<Artist>
) {
  data class Album(
    val name: String,
    val href: String,
    val albumType: String,
    val totalTracks: Int,
    val releaseDate: LocalDate,
    val images: String
  )

  data class Artist(
    val name: String,
    val href: String
  )
}
