package com.tbc_final.api_movies
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module
import com.fasterxml.jackson.databind.Module
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun hibernate6Module(): Module {
        return Hibernate6Module().apply { disable(Hibernate6Module.Feature.FORCE_LAZY_LOADING) }
    }
}