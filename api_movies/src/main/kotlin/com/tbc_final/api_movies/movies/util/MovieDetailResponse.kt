package com.tbc_final.api_movies.movies.util

import com.tbc_final.api_movies.movies.entity.Movie
import com.tbc_final.api_movies.screeing.entity.Screening

data class MovieDetailResponse(val movie: Movie, val screenings: List<Screening>)
