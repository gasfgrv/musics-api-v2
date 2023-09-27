package com.github.gasfgrv.music_api.core.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

class Utils {
  companion object {
    fun joinToString(strings: List<String>): String {
      return strings.joinToString { it }
    }

    fun encodeURL(url: String): String {
      return URLEncoder.encode(url, StandardCharsets.UTF_8)
    }

    fun toSnakeCase(string: String): String {
      return string.replace("([a-z])([A-Z]+)".toRegex(), "$1_$2").lowercase(Locale.getDefault())
    }

    fun toPascalCase(string: String): String {
      val parts = string.split("_".toRegex())
      return parts.joinToString(separator = "") { it.substring(0, 1).uppercase() + it.substring(1) }
    }

    fun logRequest(method: String = "GET", servletPath: String, requestParams: String? = null): String {
      val url = StringBuilder(servletPath)
      requestParams?.let { url.append("?$it") }
      return "Received request: [$method] $url"
    }
  }
}