package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ScanMusics(private val musicRepository: MusicRepository) {
  private val logger = LoggerFactory.getLogger(MusicRepository::class.java)

  fun scan(attributes: Map<String, String>): List<Music> {
    logger.info("Scanning musics by: $attributes")
    return musicRepository.scan(attributes)
  }

  fun hasAnyInvalidAttribute(attributes: Map<String, String>): Boolean {
    if (attributes.entries.isEmpty()) {
      return true
    }
    val allowedAtributes = listOf(
      "music_id",
      "music_name",
      "music_uri",
      "music_album",
      "music_artists",
      "music_number",
      "music_duration",
      "music_is_explicit",
      "music_popularity"
    )

    return attributes.keys.stream().anyMatch { !allowedAtributes.contains(it) }
  }
}
