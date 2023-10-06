package com.github.gasfgrv.music_api.music.data.repository

import com.github.gasfgrv.music_api.configuration.AbstractTestcontainersIntegrationTest
import com.github.gasfgrv.music_api.mocks.Mocks
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient


@ExtendWith(OutputCaptureExtension::class)
class DynamoDbMusicRepositoryTest : AbstractTestcontainersIntegrationTest() {
  private val music = Mocks.music()

  @Autowired
  private lateinit var dynamoDbMusicRepository: DynamoDbMusicRepository

  @BeforeEach
  fun setUp() {
    dynamoDbMusicRepository.save(music)
  }

  @Test
  fun save(capturedOutput: CapturedOutput) {
    val itemsCount = localStackContainer.execInContainer(
      "awslocal", "dynamodb", "scan", "--table-name", "MusicsTb"
    )
    assertThat(itemsCount.stdout).contains(music.id.toString(), music.name)
    assertThat(capturedOutput.out).contains(
      "Saving music in DynamoDB: [MusicId=${music.id.toString()}, MusicName=${music.name}]"
    )
  }

  @Test
  fun load(capturedOutput: CapturedOutput) {
    val load = dynamoDbMusicRepository.load(music.id.toString(), music.name)
    assertThat(load).isExactlyInstanceOf(music::class.java)
    assertThat(capturedOutput.out).contains(
      "Loading music in DynamoDB: [MusicId=${load!!.id}, MusicName=${load.name}]"
    )
  }

  @Test
  fun query(capturedOutput: CapturedOutput) {
    val query = dynamoDbMusicRepository.query(music.id.toString(), music.name)
    assertThat(query).usingDefaultElementComparator().hasSize(1).hasOnlyElementsOfType(music::class.java)
    assertThat(capturedOutput.out).contains(
      "Querying musics in DynamoDB: [MusicId=${music.id.toString()}, MusicName=${music.name}]"
    )
  }

  @Test
  fun scan(capturedOutput: CapturedOutput) {
    val attributes = HashMap<String, String>()
    attributes["music_number"] = music.diskNumber.toString()
    attributes["music_duration"] = music.duration!!
    val scan = dynamoDbMusicRepository.scan(attributes)
    assertThat(scan).usingDefaultElementComparator().hasSize(1).hasOnlyElementsOfType(music::class.java)
    assertThat(capturedOutput.out).contains(attributes.keys)
    assertThat(capturedOutput.out).contains(
      "Scanning table in DynamoDB for search music"
    )
  }
}