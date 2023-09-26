package com.github.gasfgrv.music_api.music.data.model.entity

import com.github.gasfgrv.music_api.core.annotations.NoArg
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@NoArg
@DynamoDbBean
data class Music(
    @get:DynamoDbPartitionKey @get:DynamoDbAttribute("MusicId") var id: String,
    @get:DynamoDbSortKey @get:DynamoDbAttribute("MusicName") var name: String,
    @get:DynamoDbAttribute("MusicUri") var uri: String,
    @get:DynamoDbAttribute("MusicAlbum") var album: Album,
    @get:DynamoDbAttribute("MusicArtists") var artists: List<Artist>,
    @get:DynamoDbAttribute("MusicNumber") var diskNumber: Int,
    @get:DynamoDbAttribute("MusicDuration") var duration: String,
    @get:DynamoDbAttribute("MusicIsExplicit") var explicit: String,
    @get:DynamoDbAttribute("MusicPopularity") var popularity: Int,
)
