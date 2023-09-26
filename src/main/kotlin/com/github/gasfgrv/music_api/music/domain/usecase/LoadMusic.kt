package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import org.springframework.stereotype.Service

@Service
class LoadMusic(private val musicRepository: MusicRepository) {
    fun load() {
        musicRepository.load("","")
    }
}
