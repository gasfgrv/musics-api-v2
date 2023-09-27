package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class QueryMusics(private val musicRepository: MusicRepository) {
  private val logger = LoggerFactory.getLogger(QueryMusics::class.java)
  fun query(id: UUID, name: String?): List<Music> {
    val musics = musicRepository.query(id.toString(), name)

    if (musics.isEmpty()) {
      logger.warn("Filtering could not find any songs, returning an empty list")
      return emptyList()
    }

    logger.info("Filter found ${musics.size} songs")
    return musics
  }
}
