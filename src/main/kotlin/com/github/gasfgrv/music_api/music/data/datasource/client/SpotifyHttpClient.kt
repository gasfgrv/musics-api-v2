package com.github.gasfgrv.music_api.music.data.datasource.client

import com.github.gasfgrv.music_api.core.utils.Utils
import com.github.gasfgrv.music_api.music.data.datasource.client.feign.AuthSpotifyFeignClient
import com.github.gasfgrv.music_api.music.data.datasource.client.feign.SpotifyFeignClient
import com.github.gasfgrv.music_api.music.data.datasource.mapper.MusicMapper
import com.github.gasfgrv.music_api.music.data.model.dto.AccessTokenResponse
import com.github.gasfgrv.music_api.music.data.model.dto.SpotifyResponse
import com.github.gasfgrv.music_api.music.domain.client.SpotifyClient
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.jayway.jsonpath.JsonPath
import java.time.LocalDate
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest

@Component
class SpotifyHttpClient(
  private val authSpotifyFeignClient: AuthSpotifyFeignClient,
  private val spotifyFeignClient: SpotifyFeignClient,
  private val musicMapper: MusicMapper,
  private val secretsManagerClient: SecretsManagerClient
) : SpotifyClient {
  private val logger = LoggerFactory.getLogger(SpotifyHttpClient::class.java)

  override fun getTrackInfo(music: Music): Music {
    val accessToken = getAccessTokenSpotify()
    val artistsName = Utils.joinToString(music.artists.map { it.name })
    val trackId = getTrackId(music.name, artistsName, accessToken)
    val response = spotifyFeignClient.getTrack(
      id = trackId,
      market = "BR",
      accessToken = "${accessToken.tokenType} ${accessToken.accessToken}"
    )

    logger.info("Returning song data")
    val spotifyResponse = mountResponseObject(response)

    return musicMapper.toDomainEntity(spotifyResponse)
  }

  private fun getAccessTokenSpotify(): AccessTokenResponse {
    val clientId = getSecret("spotify_client_id")
    val clientSecret = getSecret("spotify_client_secret")
    val request = HashMap<String, String>()
    request["grant_type"] = "client_credentials"
    request["client_id"] = clientId!!
    request["client_secret"] = clientSecret!!

    logger.info("Getting Spotify API access token")
    return authSpotifyFeignClient.getAccessToken(request)
  }

  private fun getTrackId(name: String, artist: String, accessToken: AccessTokenResponse): String {
    logger.info("Searching music data on Spotify")
    val response = spotifyFeignClient.searchTrack(
      query = Utils.encodeURL("$name $artist"),
      type = "track",
      market = "BR",
      limit = "1",
      offset = "0",
      includeExternal = "audio",
      accessToken = "${accessToken.tokenType} ${accessToken.accessToken}"
    )

    logger.info("Getting music id")
    return extractResponseAttribute(response.body!!, JSONPATH_MUSIC_ID)
  }

  private fun mountResponseObject(response: ResponseEntity<String>): SpotifyResponse {
    logger.info("Gathering track data")
    return SpotifyResponse(
      name = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_NAME),
      href = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_HREF),
      trackNumber = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_NUMBER),
      durationMs = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_DURATION),
      explicit = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_EXPLICIT),
      popularity = extractResponseAttribute(response.body!!, JSONPATH_MUSIC_POPULARITY),
      album = getAlbumResponse(response.body!!),
      artists = getArtistsResponse(response.body!!)
    )
  }

  private fun getAlbumResponse(body: String): SpotifyResponse.Album {
    logger.info("Gathering track album data")
    return SpotifyResponse.Album(
      name = extractResponseAttribute(body, JSONPATH_ALBUM_NAME),
      href = extractResponseAttribute(body, JSONPATH_ALBUM_HREF),
      albumType = extractResponseAttribute(body, JSONPATH_ALBUM_TYPE),
      totalTracks = extractResponseAttribute(body, JSONPATH_ALBUM_TOTAL_TRACKS),
      releaseDate = LocalDate.parse(extractResponseAttribute(body, JSONPATH_ALBUM_RELEASE_DATE)),
      images = extractResponseAttribute(body, JSONPATH_ALBUM_IMAGE)
    )
  }

  private fun getArtistsResponse(body: String): List<SpotifyResponse.Artist> {
    logger.info("Gathering track artists data")
    val artists = ArrayList<SpotifyResponse.Artist>()
    val artistsNames = extractResponseAttribute<List<String>>(body, JSONPATH_ARTIST_NAME)
    val artistsHrefs = extractResponseAttribute<List<String>>(body, JSONPATH_ARTIST_HREF)
    val artistAmount = extractResponseAttribute<Int>(body, JSONPATH_ARTIST_AMOUNT) - 1
    for (i in 0..artistAmount) {
      artists.add(SpotifyResponse.Artist(artistsNames[i], artistsHrefs[i]))
    }

    return artists
  }

  private fun <T> extractResponseAttribute(responseBody: String, jsonPathExpression: String): T {
    return JsonPath.parse(responseBody).read(jsonPathExpression)
  }

  private fun getSecret(secretName: String): String? {
    val valueRequest = GetSecretValueRequest.builder().secretId(secretName).build()
    return secretsManagerClient.getSecretValue(valueRequest).secretString()
  }

  private companion object Constants {
    private const val JSONPATH_MUSIC_ID = "\$.tracks.items[0].id"
    private const val JSONPATH_MUSIC_NAME = "\$.name"
    private const val JSONPATH_MUSIC_HREF = "\$.external_urls.spotify"
    private const val JSONPATH_MUSIC_NUMBER = "\$.track_number"
    private const val JSONPATH_MUSIC_DURATION = "\$.duration_ms"
    private const val JSONPATH_MUSIC_EXPLICIT = "\$.explicit"
    private const val JSONPATH_MUSIC_POPULARITY = "\$.popularity"
    private const val JSONPATH_ALBUM_NAME = "\$.album.name"
    private const val JSONPATH_ALBUM_HREF = "\$.album.external_urls.spotify"
    private const val JSONPATH_ALBUM_TYPE = "\$.album.album_type"
    private const val JSONPATH_ALBUM_TOTAL_TRACKS = "\$.album.total_tracks"
    private const val JSONPATH_ALBUM_RELEASE_DATE = "\$.album.release_date"
    private const val JSONPATH_ALBUM_IMAGE = "\$.album.images[1].url"
    private const val JSONPATH_ARTIST_NAME = "\$.artists[*].name"
    private const val JSONPATH_ARTIST_HREF = "\$.artists[*].external_urls.spotify"
    private const val JSONPATH_ARTIST_AMOUNT = "\$.artists.length()"
  }
}
