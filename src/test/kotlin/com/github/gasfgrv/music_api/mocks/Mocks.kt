package com.github.gasfgrv.music_api.mocks

import com.github.gasfgrv.music_api.music.data.model.dto.MusicRequest
import com.github.gasfgrv.music_api.music.data.model.dto.SpotifyResponse
import com.github.gasfgrv.music_api.music.domain.entity.Album
import com.github.gasfgrv.music_api.music.domain.entity.Artist
import com.github.gasfgrv.music_api.music.domain.entity.Music
import java.time.LocalDate
import java.util.UUID
import com.github.gasfgrv.music_api.music.data.model.entity.Album as AlbumEntity
import com.github.gasfgrv.music_api.music.data.model.entity.Artist as ArtistEntity
import com.github.gasfgrv.music_api.music.data.model.entity.Music as MusicEntity

class Mocks {
  companion object {
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

    fun request() = MusicRequest(
      name = "Casio",
      artists = listOf("Jungle")
    )

    fun entity() = MusicEntity(
      id = "6a29f656-2919-4cfe-a560-d8297db75b97",
      name = "Casio",
      uri = "https://open.spotify.com/track/4DqekvGQW2zHNMVh0gM3qp",
      album = AlbumEntity(
        albumName = "Summer Vibes",
        albumType = "album",
        totalTracks = 11,
        releaseDate = "2019-07-17",
        coverUrl = "https://i.scdn.co/image/ab67616d00004851c70ec669e44f753f9f067691",
        href = "https://open.spotify.com/album/4TFZwv1D8oZV04OAMBewKc"
      ),
      artists = listOf(
        ArtistEntity(
          name = "Jungle",
          href = "https://open.spotify.com/artist/3gVC68xFogCXS5AeSBnLSP"
        )
      ),
      diskNumber = 1,
      duration = "03:54",
      explicit = "No",
      popularity = 16
    )

    fun spotifyResponse() = SpotifyResponse(
      name = "Casio",
      href = "https://open.spotify.com/track/4DqekvGQW2zHNMVh0gM3qp",
      trackNumber = 1,
      durationMs = 138000,
      explicit = false,
      popularity = 16,
      album = SpotifyResponse.Album(
        name = "Summer Vibes",
        albumType = "album",
        totalTracks = 11,
        releaseDate = LocalDate.parse("2019-07-17"),
        images = "https://i.scdn.co/image/ab67616d00004851c70ec669e44f753f9f067691",
        href = "https://open.spotify.com/album/4TFZwv1D8oZV04OAMBewKc"
      ),
      artists = listOf(
        SpotifyResponse.Artist(
          name = "Jungle",
          href = "https://open.spotify.com/artist/3gVC68xFogCXS5AeSBnLSP"
        )
      )
    )
  }
}