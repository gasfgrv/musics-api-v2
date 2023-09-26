package com.github.gasfgrv.music_api.music.data.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AlbumResponse(
    val albumName: String,
    var releaseDate: String,
    val coverUrl: String,
    val href: String
)
