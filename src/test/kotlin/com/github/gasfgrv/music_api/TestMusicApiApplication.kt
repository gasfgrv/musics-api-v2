package com.github.gasfgrv.music_api

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestMusicApiApplication

fun main(args: Array<String>) {
	fromApplication<MusicApiApplication>().with(TestMusicApiApplication::class).run(*args)
}
