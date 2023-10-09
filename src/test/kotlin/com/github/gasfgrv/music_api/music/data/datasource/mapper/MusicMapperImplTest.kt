package com.github.gasfgrv.music_api.music.data.datasource.mapper

import com.github.gasfgrv.music_api.mocks.Mocks
import com.github.gasfgrv.music_api.music.data.model.dto.MusicResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicsResponse
import com.github.gasfgrv.music_api.music.domain.entity.Music
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import com.github.gasfgrv.music_api.music.data.model.entity.Music as MusicEntity

class MusicMapperImplTest {
  private val musicMapper: MusicMapper = MusicMapperImpl()

  @Test
  fun toDataEntity() {
    val entity = Mocks.music()
    val tableEntity = musicMapper.toDataEntity(entity)
    assertThat(tableEntity).isNotNull.isExactlyInstanceOf(MusicEntity::class.java)
  }

  @Test
  fun toDomainEntity() {
    val response = Mocks.spotifyResponse()
    val entity = musicMapper.toDomainEntity(response)
    assertThat(entity).isNotNull.isExactlyInstanceOf(Music::class.java)
  }

  @Test
  fun toResponseCollection() {
    val entity = Mocks.music()
    val response = musicMapper.toResponseCollection(entity)
    assertThat(response).isNotNull.isExactlyInstanceOf(MusicsResponse::class.java)
  }

  @Test
  fun testToDomainEntity() {
    val dataEntity = Mocks.entity()
    val domainEntity = musicMapper.toDomainEntity(dataEntity)
    assertThat(domainEntity).isNotNull.isExactlyInstanceOf(Music::class.java)
  }

  @Test
  fun toDomainEntityMusicRequest() {
    val request = Mocks.request()
    val entity = musicMapper.toDomainEntity(request)
    assertThat(entity).isNotNull.isExactlyInstanceOf(Music::class.java)
  }

  @Test
  fun toResponse() {
    val entity = Mocks.music()
    val response = musicMapper.toResponse(entity)
    assertThat(response).isNotNull.isExactlyInstanceOf(MusicResponse::class.java)
  }
}
