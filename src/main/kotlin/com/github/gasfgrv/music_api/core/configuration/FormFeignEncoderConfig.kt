package com.github.gasfgrv.music_api.core.configuration

import feign.codec.Encoder
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FormFeignEncoderConfig {
    @Bean
    fun encoder(converters: ObjectFactory<HttpMessageConverters>): Encoder {
        return SpringFormEncoder(SpringEncoder(converters))
    }
}