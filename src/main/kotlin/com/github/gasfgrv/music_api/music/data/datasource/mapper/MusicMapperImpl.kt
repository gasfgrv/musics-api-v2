package com.github.gasfgrv.music_api.music.data.datasource.mapper

import com.github.gasfgrv.music_api.music.data.model.dto.AlbumResponse
import com.github.gasfgrv.music_api.music.data.model.dto.ArtistResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicRequest
import com.github.gasfgrv.music_api.music.data.model.dto.MusicResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicsResponse
import com.github.gasfgrv.music_api.music.data.model.dto.SpotifyResponse
import com.github.gasfgrv.music_api.music.domain.entity.Album
import com.github.gasfgrv.music_api.music.domain.entity.Artist
import com.github.gasfgrv.music_api.music.domain.entity.Music
import java.time.LocalDate
import java.util.UUID
import org.springframework.stereotype.Component
import com.github.gasfgrv.music_api.music.data.model.entity.Album as AlbumEntity
import com.github.gasfgrv.music_api.music.data.model.entity.Artist as ArtistEntity
import com.github.gasfgrv.music_api.music.data.model.entity.Music as MusicEntity

@Component
class MusicMapperImpl : MusicMapper {
    override fun toDataEntity(music: Music): MusicEntity {
        val artists = music.artists.map { ArtistEntity(it.name, it.href!!) }

        val album = AlbumEntity(
            albumName = music.album!!.albumName,
            albumType = music.album.albumType!!,
            totalTracks = music.album.totalTracks!!,
            releaseDate = music.album.releaseDate.toString(),
            coverUrl = music.album.coverUrl!!,
            href = music.album.href!!
        )

        val explicit = when (music.explicit!!) {
            true -> "Yes"
            false -> "No"
        }
        return MusicEntity(
            id = music.id.toString(),
            name = music.name,
            uri = music.uri!!,
            album = album,
            artists = artists,
            diskNumber = music.diskNumber!!,
            duration = music.duration!!,
            explicit = explicit,
            popularity = music.popularity!!
        )
    }

    override fun toDomainEntity(request: MusicRequest): Music {
        return Music(
            name = request.name,
            artists = request.artists.map { Artist(name = it) }
        )
    }

    override fun toResponseCollection(music: Music): MusicsResponse {
        return MusicsResponse(
            name = music.name,
            uri = music.uri,
            diskNumber = music.diskNumber,
            duration = music.duration,
            explicit = music.explicit,
            popularity = music.popularity
        )
    }

    override fun toDomainEntity(music: MusicEntity): Music {
        val album = Album(
            albumName = music.album.albumName,
            albumType = music.album.albumType,
            totalTracks = music.album.totalTracks,
            releaseDate = LocalDate.parse(music.album.releaseDate),
            coverUrl = music.album.coverUrl,
            href = music.album.href
        )
        val artists = music.artists
            .map { Artist(it.name, it.href) }

        val explicit = when (music.explicit) {
            "Yes" -> true
            "No" -> false
            else -> null
        }

        return Music(
            id = UUID.fromString(music.id),
            name = music.name,
            uri = music.uri,
            album = album,
            artists = artists,
            diskNumber = music.diskNumber,
            duration = music.duration,
            explicit = explicit,
            popularity = music.popularity
        )
    }

    override fun toDomainEntity(spotifySongData: SpotifyResponse): Music {
        val albumData = Album(
            albumName = spotifySongData.album.name,
            albumType = spotifySongData.album.albumType,
            totalTracks = spotifySongData.album.totalTracks,
            releaseDate = spotifySongData.album.releaseDate,
            coverUrl = spotifySongData.album.images,
            href = spotifySongData.album.href
        )

        val artistsData = spotifySongData.artists
            .map { Artist(it.name, it.href) }

        return Music(
            name = spotifySongData.name,
            uri = spotifySongData.href,
            album = albumData,
            artists = artistsData,
            diskNumber = spotifySongData.trackNumber,
            duration = Music.convertToMinutes(spotifySongData.durationMs),
            explicit = spotifySongData.explicit,
            popularity = spotifySongData.popularity
        )
    }

    override fun toResponse(savedMusic: Music): MusicResponse {
        val albumResponse = AlbumResponse(
            albumName = savedMusic.album!!.albumName,
            releaseDate = savedMusic.album.releaseDate.toString(),
            coverUrl = savedMusic.album.coverUrl!!,
            href = savedMusic.album.href!!
        )

        val artistsRespose = savedMusic.artists
            .map { ArtistResponse(it.name, it.href!!) }

        return MusicResponse(
            name = savedMusic.name,
            uri = savedMusic.uri!!,
            album = albumResponse,
            artists = artistsRespose,
            diskNumber = savedMusic.diskNumber!!,
            duration = savedMusic.duration!!,
            explicit = savedMusic.explicit!!,
            popularity = savedMusic.popularity!!,
        )
    }
}