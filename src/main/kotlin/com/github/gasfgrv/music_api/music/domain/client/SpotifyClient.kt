package com.github.gasfgrv.music_api.music.domain.client

import com.github.gasfgrv.music_api.music.domain.entity.Music

fun interface SpotifyClient {
  fun getTrackInfo(music: Music): Music
}
