package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.movies.entity.Movie

data class MovieDetailDTO(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val genres: List<GenreDTO>,
    val actors: List<ActorDTO>
)

fun Movie.toDetailDto() = MovieDetailDTO(
    id = this.id,
    title = this.title,
    description = this.description,
    duration = this.duration,
    genres = this.genres.map { GenreDTO(it.id, it.name) },
    actors = this.actors.map {
        ActorDTO(
            id = it.id,
            name = it.name,
            bio = it.bio
        )
    }
)