package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.movies.entity.Movie
import com.tbc_final.api_movies.screeing.dto.ScreeningDTO
import java.math.BigDecimal

data class MovieDetailDTO(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val ageRestriction: String,
    val movieImgUrl: String,
    val director: String,
    val imdbRating: BigDecimal,
    val genres: List<GenreDTO>,
    val actors: List<ActorDTO>,
    val screenings: List<ScreeningDTO> // new field
)

fun Movie.toDetailDto(screenings: List<ScreeningDTO>) = MovieDetailDTO(
    id = this.id,
    title = this.title,
    description = this.description,
    duration = this.duration,
    genres = this.genres.map { GenreDTO(it.id, it.name) },
    actors = this.actors.map {
        ActorDTO(
            id = it.id,
            name = it.name,
            actorImgUrl = it.actorImgUrl
        )
    },
    ageRestriction = this.ageRestriction,
    movieImgUrl = this.movieImgUrl,
    imdbRating = this.imdbRating,
    screenings = screenings,
    director = this.director

)