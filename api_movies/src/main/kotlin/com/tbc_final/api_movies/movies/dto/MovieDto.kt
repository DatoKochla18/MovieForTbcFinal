package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.movies.entity.Movie

data class MovieDTO(
    val id: Int,
    val title: String,
    val description: String,
    val movieImgUrl:String,
    val duration: Int
)

fun Movie.toDTO() = MovieDTO(
    id = this.id,
    title = this.title,
    movieImgUrl = this.movieImgUrl,
    description = this.description,
    duration = this.duration
)