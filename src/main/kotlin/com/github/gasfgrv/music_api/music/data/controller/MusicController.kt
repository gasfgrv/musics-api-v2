package com.github.gasfgrv.music_api.music.data.controller

import com.github.gasfgrv.music_api.core.exception.InvalidParametersException
import com.github.gasfgrv.music_api.core.utils.Utils
import com.github.gasfgrv.music_api.music.data.datasource.mapper.MusicMapper
import com.github.gasfgrv.music_api.music.data.model.dto.MusicRequest
import com.github.gasfgrv.music_api.music.data.model.dto.MusicResponse
import com.github.gasfgrv.music_api.music.data.model.dto.MusicsResponse
import com.github.gasfgrv.music_api.music.domain.usecase.SaveMusic
import com.github.gasfgrv.music_api.music.domain.usecase.ScanMusics
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
@RequestMapping("/musics/v2")
@Tag(name = "Music")
class MusicController(
    private val musicMapper: MusicMapper,
    private val saveMusic: SaveMusic,
    private val scanMusics: ScanMusics
) {
    private val logger = LoggerFactory.getLogger(MusicController::class.java)

    @PostMapping("/save")
    fun saveMusic(
        @RequestBody @Valid request: MusicRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<MusicResponse> {
        logger.info("Received request: [${httpRequest.method}] ${httpRequest.servletPath}")
        val domainEntity = musicMapper.toDomainEntity(request)
        val savedMusic = saveMusic.save(domainEntity)

        logger.info("Mounting the 'Location' header in response")
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/find")
            .queryParam("music_name", request.name)
            .queryParam("music_artists", Utils.encodeURL(Utils.joinToString(request.artists)))
            .build()
            .toUri()

        logger.info("Mounting the response body")
        val musicResponse = musicMapper.toResponse(savedMusic)

        logger.info("Gathering response data")
        return ResponseEntity.created(location).body(musicResponse)
    }

    @GetMapping("/scan")
    fun scanMusic(
        @RequestParam attributes: Map<String, String>,
        httpRequest: HttpServletRequest
    ): ResponseEntity<List<MusicsResponse>> {
        logger.info("Received request: [${httpRequest.method}] ${httpRequest.servletPath}?${httpRequest.queryString}")

        logger.info("Checking if attributes is valid")
        if (scanMusics.isAllAttributesValid(attributes).not()) {
            logger.error("This request has one or more invalid attributes")
            throw InvalidParametersException()
        }

        val musicList = scanMusics.scan(attributes)

        logger.info("Mounting the response body")
        val response = musicList.map { musicMapper.toResponseCollection(it) }

        logger.info("Gathering response data")
        return ResponseEntity.ok(response)
    }
}
