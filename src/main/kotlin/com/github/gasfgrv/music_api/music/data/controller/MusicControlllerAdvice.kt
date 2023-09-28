package com.github.gasfgrv.music_api.music.data.controller

import com.github.gasfgrv.music_api.core.exception.InvalidParametersException
import com.github.gasfgrv.music_api.core.exception.MusicAlreadySavedException
import com.github.gasfgrv.music_api.core.exception.MusicNotFoundException
import com.github.gasfgrv.music_api.music.data.model.dto.ErrorResponse
import java.net.URI
import java.time.OffsetDateTime
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class MusicControlllerAdvice {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(
    exception: MethodArgumentNotValidException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val response = mountErrorResponse(status, exception.body.detail!!, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleHttpMessageNotReadableException(
    exception: HttpMessageNotReadableException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val detail = "Null value passed to a parameter that is not null-safety."
    val response = mountErrorResponse(status, detail, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException::class)
  fun handleMissingServletRequestParameterException(
    exception: MissingServletRequestParameterException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val response = mountErrorResponse(status, exception.body.detail!!, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgumentException(
    exception: IllegalArgumentException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val response = mountErrorResponse(status, exception.localizedMessage, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MusicAlreadySavedException::class)
  fun handleMusicAlreadySavedException(
    exception: MusicAlreadySavedException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val response = mountErrorResponse(status, exception.localizedMessage, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidParametersException::class)
  fun handleInvalidParametersException(
    exception: InvalidParametersException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.BAD_REQUEST
    val headers = headers()
    val response = mountErrorResponse(status, exception.localizedMessage, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(MusicNotFoundException::class)
  fun handleMusicNotFoundException(
    exception: MusicNotFoundException,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.NOT_FOUND
    val headers = headers()
    val response = mountErrorResponse(status, exception.localizedMessage, request)
    return handleException(status, headers, response)
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception::class)
  fun handleNotMappedException(
    exception: Exception,
    request: WebRequest
  ): ResponseEntity<ErrorResponse> {
    val status = HttpStatus.INTERNAL_SERVER_ERROR
    val headers = headers()
    val response = mountErrorResponse(status, exception.localizedMessage, request)
    return handleException(status, headers, response)
  }

  private fun headers(): HttpHeaders {
    val headers = HttpHeaders()
    headers[HttpHeaders.CONTENT_TYPE] = mutableListOf(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    return headers
  }

  private fun mountErrorResponse(
    status: HttpStatus,
    message: String,
    request: WebRequest
  ): ErrorResponse {
    return ErrorResponse(
      title = status.reasonPhrase,
      detail = message,
      instance = URI.create((request as ServletWebRequest).request.requestURI.toString()),
      status = status.value(),
      timestamp = OffsetDateTime.now()
    )
  }

  private fun handleException(
    status: HttpStatus,
    headers: HttpHeaders,
    response: ErrorResponse
  ): ResponseEntity<ErrorResponse> {
    return ResponseEntity.status(status).headers(headers).body(response)
  }
}