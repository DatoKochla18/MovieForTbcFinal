package com.tbc_final.api_movies

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        // Register custom converters for LocalDateTime
        registry.addConverter(StringToLocalDateTimeConverter())
    }

    class StringToLocalDateTimeConverter : Converter<String, LocalDateTime> {
        override fun convert(source: String): LocalDateTime {
            return LocalDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME)
        }
    }
}