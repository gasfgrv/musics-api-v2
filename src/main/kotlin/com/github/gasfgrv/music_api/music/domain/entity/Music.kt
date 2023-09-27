package com.github.gasfgrv.music_api.music.domain.entity

import java.util.UUID

data class Music(
  var id: UUID? = null,
  val name: String,
  val uri: String? = null,
  val album: Album? = null,
  val artists: List<Artist>,
  val diskNumber: Int? = null,
  val duration: String? = null,
  val explicit: Boolean? = null,
  val popularity: Int? = null
) {
  fun generateId() {
    this.id = when (this.id) {
      null -> UUID.randomUUID()
      else -> this.id
    }
  }

  companion object {
    fun convertToMinutes(durationMs: Int): String {
      val minutes = (durationMs / 1000 / 60).toString()
      val seconds = (durationMs / 1000 % 60).toString()
      return "${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
    }
  }
}
