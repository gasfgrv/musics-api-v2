package com.github.gasfgrv.music_api.music.domain.usecase

import com.github.gasfgrv.music_api.core.exception.MusicNotFoundException
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import java.util.Optional
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class LoadMusic(private val musicRepository: MusicRepository) {
    fun load(id: UUID, name: String?): Music? {
        return Optional.ofNullable(musicRepository.load(id.toString(), name))
            .orElseThrow { MusicNotFoundException() }
    }
}
