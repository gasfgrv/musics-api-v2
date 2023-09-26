package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.core.exception.MusicAlreadySavedException
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SaveMusic(
    private val musicRepository: MusicRepository,
    private val getTrackDetails: GetTrackDetails
) {
    private val logger = LoggerFactory.getLogger(SaveMusic::class.java)

    fun save(music: Music): Music {
        val musicToSave = getTrackDetails.getTrack(music)

        val attributes = HashMap<String, String>()
        attributes["music_name"] = musicToSave.name

        logger.info("Checking if music already exists")
        if (musicRepository.scan(attributes).isNotEmpty()) {
            throw MusicAlreadySavedException()
        }

        logger.info("Setting music id")
        musicToSave.generateId()
        musicRepository.save(musicToSave)

        return musicToSave
    }
}
