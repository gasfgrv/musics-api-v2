package com.github.gasfgrv.music_api.music.data.repository

import com.github.gasfgrv.music_api.core.utils.Utils
import com.github.gasfgrv.music_api.music.data.datasource.mapper.MusicMapper
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import com.github.gasfgrv.music_api.music.data.model.entity.Music as MusicEntity

@Repository
class DynamoDbMusicRepository(
  private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
  private val musicMapper: MusicMapper
) : MusicRepository {
  private val logger = LoggerFactory.getLogger(DynamoDbMusicRepository::class.java)
  override fun save(music: Music) {
    logger.info("Saving music in DynamoDB: [MusicId=${music.id.toString()}, MusicName=${music.name}]")
    val musicEntity = musicMapper.toDataEntity(music)
    getTable().putItem(musicEntity)
  }

  override fun load(id: String, name: String): Music? {
    val request = GetItemEnhancedRequest.builder()
      .key(setKey(id, name))
      .build()

    logger.info("Loading music in DynamoDB: [MusicId=${id}, MusicName=${name}]")
    val music = getTable().getItem(request) ?: return null

    return musicMapper.toDomainEntity(music)
  }

  override fun query(id: String, name: String?): List<Music> {
    val queryConditional = QueryConditional.keyEqualTo(setKey(id, name))

    logger.info("Querying musics in DynamoDB: [MusicId=${id}, MusicName=${name}]")
    return getTable().query(queryConditional)
      .items()
      .stream()
      .toList()
      .map { musicMapper.toDomainEntity(it) }
  }

  override fun scan(attributes: Map<String, Any>): List<Music> {
    logger.info("Gathering filter conditions");
    val joinedConditions = setConditions(attributes.keys)
    val attributesValue = attributes.map {
      ":${Utils.toSnakeCase(it.key)}" to setAttributeValue(it.key, it.value)
    }.toMap()

    logger.info("Mounting of filter expression: [$joinedConditions]")
    val filterExpression = Expression.builder()
      .expression(joinedConditions)
      .expressionValues(attributesValue)
      .build()
    val scanRequest = ScanEnhancedRequest.builder()
      .filterExpression(filterExpression)
      .build()

    logger.info("Scanning table in DynamoDB for search music")
    return getTable().scan(scanRequest)
      .items()
      .stream()
      .toList()
      .map { musicMapper.toDomainEntity(it) }
  }

  private fun getTable(): DynamoDbTable<MusicEntity> {
    val tableSchema = TableSchema.fromBean(MusicEntity::class.java)
    return dynamoDbEnhancedClient.table("MusicsTb", tableSchema)
  }

  private fun setKey(id: String, name: String?): Key {
    return when (name) {
      null -> Key.builder().partitionValue(id).build()
      else -> Key.builder().partitionValue(id).sortValue(name).build()
    }
  }

  private fun setConditions(keys: Set<String>): String {
    return keys.joinToString(separator = " and ") {
      when (it) {
        "music_artists" -> "MusicArtists[0].ArtistName = :${Utils.toSnakeCase(it)}"
        "music_album" -> "MusicAlbum.AlbumName = :${Utils.toSnakeCase(it)}"
        else -> "${Utils.toPascalCase(it)} = :${Utils.toSnakeCase(it)}"
      }
    }
  }

  private fun setAttributeValue(key: String, value: Any): AttributeValue {
    return when (key) {
      "music_number", "music_popularity" -> AttributeValue.fromN(value as String)
      else -> AttributeValue.fromS(value as String)
    }
  }
}