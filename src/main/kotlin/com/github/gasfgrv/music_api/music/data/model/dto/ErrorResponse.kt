package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.net.URI
import java.time.OffsetDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ErrorResponse(
  val title: String,
  val detail: String,
  val instance: URI,
  val status: Int,
  val timestamp: OffsetDateTime
)