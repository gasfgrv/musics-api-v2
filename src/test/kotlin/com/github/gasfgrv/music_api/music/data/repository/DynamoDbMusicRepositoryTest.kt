package com.github.gasfgrv.music_api.music.data.repository

import com.github.gasfgrv.music_api.configuration.AbstractTestcontainersIntegrationTest
import com.github.gasfgrv.music_api.mocks.Mocks
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DynamoDbMusicRepositoryTest : AbstractTestcontainersIntegrationTest() {
  private val music = Mocks.music()

  @Autowired
  private lateinit var dynamoDbMusicRepository: DynamoDbMusicRepository

  @Test
  @Order(1)
  fun save(capturedOutput: CapturedOutput) {
    dynamoDbMusicRepository.save(music)
    val itemsCount = localStackContainer.execInContainer(
      "awslocal", "dynamodb", "scan", "--table-name", "MusicsTb"
    )
    assertThat(itemsCount.stdout)
      .contains(music.id.toString(), music.name)
    assertThat(capturedOutput.out)
      .contains("Saving music in DynamoDB: [MusicId=${music.id.toString()}, MusicName=${music.name}]")
  }

  @Test
  @Order(2)
  fun load(capturedOutput: CapturedOutput) {
    val load = dynamoDbMusicRepository.load(music.id.toString(), music.name)
    assertThat(load)
      .isExactlyInstanceOf(music::class.java)
    assertThat(capturedOutput.out)
      .contains("Loading music in DynamoDB: [MusicId=${load!!.id}, MusicName=${load.name}]")
  }

  @Test
  @Order(3)
  fun query(capturedOutput: CapturedOutput) {
    val query = dynamoDbMusicRepository.query(music.id.toString(), music.name)
    assertThat(query)
      .usingDefaultElementComparator()
      .hasSize(1)
      .hasOnlyElementsOfType(music::class.java)
    assertThat(capturedOutput.out)
      .contains("Querying musics in DynamoDB: [MusicId=${music.id.toString()}, MusicName=${music.name}]")
  }

  @Test
  @Order(4)
  fun scan(capturedOutput: CapturedOutput) {
    val attributes = HashMap<String, String>()
    attributes["music_number"] = music.diskNumber.toString()
    attributes["music_duration"] = music.duration!!
    val scan = dynamoDbMusicRepository.scan(attributes)
    assertThat(scan)
      .usingDefaultElementComparator()
      .hasSize(1)
      .hasOnlyElementsOfType(music::class.java)
    assertThat(capturedOutput.out)
      .contains(attributes.keys)
    assertThat(capturedOutput.out)
      .contains("Scanning table in DynamoDB for search music")
  }
}