package com.github.gasfgrv.music_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class MusicApiApplication

fun main(args: Array<String>) {
    runApplication<MusicApiApplication>(*args)
}
