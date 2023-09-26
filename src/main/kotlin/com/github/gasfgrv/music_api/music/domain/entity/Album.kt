package com.github.gasfgrv.music_api.music.domain.entity

import java.time.LocalDate

data class Album(
    val albumName: String,
    val albumType: String? = null,
    val totalTracks: Int? = null,
    var releaseDate: LocalDate? = null,
    val coverUrl: String? = null,
    val href: String? = null,
)
