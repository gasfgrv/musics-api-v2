package com.github.gasfgrv.music_api.music.data.model.entity

import com.github.gasfgrv.music_api.core.annotations.NoArg
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@NoArg
@DynamoDbBean
data class Artist(
  @get:DynamoDbAttribute("ArtistName") var name: String,
  @get:DynamoDbAttribute("ArtistUri") var href: String
)
