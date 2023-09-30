package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MusicsResponse(
  val name: String,
  val uri: String? = null,
  val diskNumber: Int? = null,
  val duration: String? = null,
  val explicit: Boolean? = null,
  val popularity: Int? = null
)
