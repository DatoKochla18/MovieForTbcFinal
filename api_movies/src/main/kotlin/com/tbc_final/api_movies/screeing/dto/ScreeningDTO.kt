package com.tbc_final.api_movies.screeing.dto

import java.time.LocalDateTime

data class ScreeningDTO(
    val id: Int,
    val screeningTime: LocalDateTime
)

