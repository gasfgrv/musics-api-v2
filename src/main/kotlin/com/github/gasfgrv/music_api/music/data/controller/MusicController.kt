package com.github.gasfgrv.music_api.music.data.controller

import com.github.gasfgrv.music_api.core.exception.InvalidParametersException
import com.github.gasfgrv.music_api.core.utils.Utils
import com.github.gasfgrv.music_api.music.data.datasource.mapper.MusicMapper
import com.github.gasfgrv.music_api.music.data.model.dto.MusicRequest
import com.github.gasfgrv.music_api.music.data.model.dto.MusicResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicsResponse
import com.github.gasfgrv.music_api.music.domain.usecase.LoadMusic
import com.github.gasfgrv.music_api.music.domain.usecase.QueryMusics
import com.github.gasfgrv.music_api.music.domain.usecase.SaveMusic
import com.github.gasfgrv.music_api.music.domain.usecase.ScanMusics
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

private const val MOUNTING_RESPONSE = "Mounting the response body"
private const val GATHERING_RESPONSE = "Gathering response data"

@RestController
@RequestMapping("/musics/v2")
@Tag(name = "Musics")
class MusicController(
  private val musicMapper: MusicMapper,
  private val saveMusic: SaveMusic,
  private val scanMusics: ScanMusics,
  private val queryMusics: QueryMusics,
  private val loadMusic: LoadMusic
) {
  private val logger = LoggerFactory.getLogger(MusicController::class.java)

  @PostMapping(
    value = ["/save"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun saveMusic(
    @Valid @RequestBody request: MusicRequest,
    httpRequest: HttpServletRequest
  ): ResponseEntity<MusicResponse> {
    println(request)
    logger.info(Utils.logRequest(method = httpRequest.method, servletPath = httpRequest.servletPath))
    val domainEntity = musicMapper.toDomainEntity(request)
    val savedMusic = saveMusic.save(domainEntity)

    logger.info("Mounting the 'Location' header in response")
    val location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/load")
      .pathSegment(savedMusic.id.toString())
      .queryParam("music_name", Utils.encodeURL(savedMusic.name))
      .build()
      .toUri()

    logger.info(MOUNTING_RESPONSE)
    val response = musicMapper.toResponse(savedMusic)

    logger.info(GATHERING_RESPONSE)
    return ResponseEntity.created(location).body(response)
  }

  @GetMapping(value = ["/scan"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun scanMusic(
    @RequestParam attributes: Map<String, String>,
    httpRequest: HttpServletRequest
  ): ResponseEntity<List<MusicsResponse>> {
    logger.info(Utils.logRequest(servletPath = httpRequest.servletPath, requestParams = httpRequest.queryString))

    logger.info("Checking if attributes is valid")
    if (scanMusics.hasAnyInvalidAttribute(attributes)) {
      logger.error("This request has one or more invalid attributes")
      throw InvalidParametersException()
    }

    logger.info(MOUNTING_RESPONSE)
    val response = scanMusics.scan(attributes).map { musicMapper.toResponseCollection(it) }

    logger.info(GATHERING_RESPONSE)
    return ResponseEntity.ok(response)
  }

  @GetMapping(value = ["/query"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun queryMusic(
    @RequestParam("music_id") id: String,
    @RequestParam("music_name", required = false) name: String?,
    httpRequest: HttpServletRequest
  ): ResponseEntity<List<MusicsResponse>> {
    logger.info(Utils.logRequest(servletPath = httpRequest.servletPath, requestParams = httpRequest.queryString))

    logger.info(MOUNTING_RESPONSE)
    val response = queryMusics.query(UUID.fromString(id), name).map { musicMapper.toResponseCollection(it) }

    logger.info(GATHERING_RESPONSE)
    return ResponseEntity.ok(response)
  }

  @GetMapping(value = ["/load/{music_id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
  fun loadMusic(
    @PathVariable("music_id") id: String,
    @RequestParam("music_name") name: String,
    httpRequest: HttpServletRequest
  ): ResponseEntity<MusicResponse> {
    logger.info(Utils.logRequest(servletPath = httpRequest.servletPath, requestParams = httpRequest.queryString))
    val music = loadMusic.load(UUID.fromString(id), name)

    logger.info(MOUNTING_RESPONSE)
    val response = musicMapper.toResponse(music!!)

    logger.info(GATHERING_RESPONSE)
    return ResponseEntity.ok(response)
  }
}
