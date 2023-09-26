package com.github.gasfgrv.music_api.music.data.model.entity

import com.github.gasfgrv.music_api.core.annotations.NoArg
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@NoArg
@DynamoDbBean
data class Album(
    @get:DynamoDbAttribute("AlbumName") var albumName: String,
    @get:DynamoDbAttribute("AlbumType") var albumType: String,
    @get:DynamoDbAttribute("AlbumTotalTracks") var totalTracks: Int,
    @get:DynamoDbAttribute("AlbumReleaseDate") var releaseDate: String,
    @get:DynamoDbAttribute("AlbumCover") var coverUrl: String,
    @get:DynamoDbAttribute("AlbumUri") var href: String
)
