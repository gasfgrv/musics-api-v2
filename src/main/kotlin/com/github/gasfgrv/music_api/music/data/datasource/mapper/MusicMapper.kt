package com.github.gasfgrv.music_api.music.data.datasource.mapper

import com.github.gasfgrv.music_api.music.data.model.dto.MusicRequest
import com.github.gasfgrv.music_api.music.data.model.dto.MusicResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicsResponse
import com.github.gasfgrv.music_api.music.data.model.dto.SpotifyResponse
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.data.model.entity.Music as MusicEntity

interface MusicMapper {
  fun toDomainEntity(spotifySongData: SpotifyResponse): Music
  fun toDomainEntity(music: MusicEntity): Music
  fun toDomainEntity(request: MusicRequest): Music
  fun toResponse(savedMusic: Music): MusicResponse
  fun toResponseCollection(music: Music): MusicsResponse
  fun toDataEntity(music: Music): MusicEntity
}
