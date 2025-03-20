package com.tbc_final.api_movies.screeing.dto

import com.tbc_final.api_movies.screeing.entity.Screening
import java.time.LocalDateTime

data class ScreeningDTO(
    val id: Int,
    val movieId: Int,
    val movieTitle: String,
    val screeningTime: LocalDateTime
)

// Extension function to convert Screening entity to DTO
fun Screening.toDTO() = ScreeningDTO(
    id = this.id,
    movieId = this.movie.id,
    movieTitle = this.movie.title,
    screeningTime = this.screeningTime
)