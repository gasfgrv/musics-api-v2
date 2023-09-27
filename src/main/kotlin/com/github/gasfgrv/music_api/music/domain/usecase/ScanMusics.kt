package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.core.utils.Utils
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

  fun isAllAttributesValid(attributes: Map<String, String>): Boolean {
    val allowedAtributes = listOf(
      "MusicId",
      "MusicName",
      "MusicUri",
      "MusicAlbum",
      "MusicArtists",
      "MusicNumber",
      "MusicDuration",
      "MusicIsExplicit",
      "MusicPopularity"
    ).map { Utils.toSnakeCase(it) }

    return attributes.keys.stream()
      .allMatch { allowedAtributes.contains(it) }
  }
}
