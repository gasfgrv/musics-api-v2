package com.github.gasfgrv.music_api.music.domain.repository

import com.github.gasfgrv.music_api.music.domain.entity.Music

interface MusicRepository {
  fun save(music: Music)
  fun load(id: String, name: String): Music?
  fun query(id: String, name: String?): List<Music>
  fun scan(attributes: Map<String, Any>): List<Music>
}
