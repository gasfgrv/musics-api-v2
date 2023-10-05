package com.github.gasfgrv.music_api.mocks

import com.github.gasfgrv.music_api.music.domain.entity.Album
import com.github.gasfgrv.music_api.music.domain.entity.Artist
import com.github.gasfgrv.music_api.music.domain.entity.Music
import java.time.LocalDate
import java.util.UUID

class Mocks {
  companion object Music {
    fun music() = Music(
      id = UUID.fromString("6a29f656-2919-4cfe-a560-d8297db75b97"),
      name = "Casio",
      uri = "https://open.spotify.com/track/4DqekvGQW2zHNMVh0gM3qp",
      album = Album(
        albumName = "Summer Vibes",
        albumType = "album",
        totalTracks = 11,
        releaseDate = LocalDate.parse("2019-07-17"),
        coverUrl = "https://i.scdn.co/image/ab67616d00004851c70ec669e44f753f9f067691",
        href = "https://open.spotify.com/album/4TFZwv1D8oZV04OAMBewKc"
      ),
      artists = listOf(
        Artist(
          name = "Jungle",
          href = "https://open.spotify.com/artist/3gVC68xFogCXS5AeSBnLSP"
        )
      ),
      diskNumber = 1,
      duration = "03:54",
      explicit = false,
      popularity = 16
    )
  }
}