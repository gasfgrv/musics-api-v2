package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.music.domain.client.SpotifyClient
import com.github.gasfgrv.music_api.music.domain.entity.Music
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetTrackDetails(private val spotifyClient: SpotifyClient) {
  private val logger = LoggerFactory.getLogger(GetTrackDetails::class.java)

  fun getTrack(music: Music): Music {
    logger.info("Consulting Spotify API to retrieve additional info about song")
    return spotifyClient.getTrackInfo(music)
  }
}