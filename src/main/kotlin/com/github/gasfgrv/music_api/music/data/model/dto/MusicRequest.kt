package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.github.gasfgrv.music_api.core.annotations.NotEmptyList
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MusicRequest(
  @field:NotBlank val name: String,
  @field:NotEmptyList val artists: List<String>
)
