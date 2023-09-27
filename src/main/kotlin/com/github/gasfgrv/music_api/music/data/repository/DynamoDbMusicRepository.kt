package com.github.gasfgrv.music_api.music.data.repository

import com.github.gasfgrv.music_api.core.utils.Utils
import com.github.gasfgrv.music_api.music.data.datasource.mapper.MusicMapper
import com.github.gasfgrv.music_api.music.domain.entity.Music
import com.github.gasfgrv.music_api.music.domain.repository.MusicRepository
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
    override fun save(music: Music) {
        val musicEntity = musicMapper.toDataEntity(music)
        getTable().putItem(musicEntity)
    }

    override fun load(id: String, name: String?): Music? {
        val key = if (name == null) {
            Key.builder()
                .partitionValue(id)
                .build()
        } else {
            Key.builder()
                .partitionValue(id)
                .sortValue(name)
                .build()
        }

        val request = GetItemEnhancedRequest.builder()
            .key(key)
            .build()

        val music = getTable().getItem(request) ?: return null

        return musicMapper.toDomainEntity(music)
    }

    override fun query(id: String, name: String?): List<Music> {
        val key = if (name == null) {
            Key.builder()
                .partitionValue(id)
                .build()
        } else {
            Key.builder()
                .partitionValue(id)
                .sortValue(name)
                .build()
        }

        val queryConditional = QueryConditional.keyEqualTo(key)

        val items = getTable().query(queryConditional)
            .items()
            .stream()
            .toList()

        return items.map { musicMapper.toDomainEntity(it) }
    }

    override fun scan(attributes: Map<String, Any>): List<Music> {
        val joinedConditions = setCondtions(attributes.keys)

        val attributesValue = attributes.map {
            ":${Utils.toSnakeCase(it.key)}" to setAttributeValue(it.key, it.value)
        }.toMap()

        val filterExpression = Expression.builder()
            .expression(joinedConditions)
            .expressionValues(attributesValue)
            .build()

        val scanRequest = ScanEnhancedRequest.builder()
            .filterExpression(filterExpression)
            .build()

        val items = getTable().scan(scanRequest)
            .items()
            .stream()
            .toList()

        return items.map { musicMapper.toDomainEntity(it) }
    }

    private fun getTable(): DynamoDbTable<MusicEntity> {
        val tableSchema = TableSchema.fromBean(MusicEntity::class.java)
        return dynamoDbEnhancedClient.table("MusicsTb", tableSchema)
    }

    private fun setCondtions(keys: Set<String>): String {
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