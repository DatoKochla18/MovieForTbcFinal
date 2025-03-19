package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.screeing.dto.ScreeningDTO

data class MovieDetailDTO(
    val movie: MovieDTO,
    val screenings: List<ScreeningDTO>
)