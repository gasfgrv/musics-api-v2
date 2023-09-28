package com.github.gasfgrv.music_api.music.data.model.dto

import java.net.URI
import java.time.OffsetDateTime

data class ErrorResponse(
  val title: String,
  val detail: String,
  val instance: URI,
  val status: Int,
  val timestamp: OffsetDateTime
)