package com.tbc_final.api_movies.screeing.dto

import com.tbc_final.api_movies.screeing.entity.Screening
import java.time.LocalDateTime

data class ScreeningDTO(
    val id: Int,
    val screeningTime: LocalDateTime,
    val movieTitle: String
)

fun Screening.toDTO() = ScreeningDTO(
    id = this.id,
    screeningTime = this.screeningTime,
    movieTitle = this.movie.title
)