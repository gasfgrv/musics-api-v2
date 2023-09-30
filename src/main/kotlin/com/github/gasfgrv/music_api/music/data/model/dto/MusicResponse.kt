package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MusicResponse(
  val name: String,
  val uri: String,
  val album: AlbumResponse,
  val artists: List<ArtistResponse>,
  val diskNumber: Int,
  val duration: String,
  val explicit: Boolean,
  val popularity: Int
)
