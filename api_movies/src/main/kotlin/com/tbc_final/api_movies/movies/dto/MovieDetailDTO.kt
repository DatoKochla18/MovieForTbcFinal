package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.movies.entity.Movie

data class MovieDetailDTO(
    val id: Int,
    val title: String,
    val description: String?,
    val duration: Int,
    val actors: List<ActorDTO>
)

fun Movie.toDetailDto(): MovieDetailDTO {
    return MovieDetailDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        duration = this.duration,
        actors = this.actors.map {
            ActorDTO(it.id, it.name, it.bio)
        }
    )
}