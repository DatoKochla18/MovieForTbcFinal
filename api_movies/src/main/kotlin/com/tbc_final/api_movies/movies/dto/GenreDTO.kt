package com.tbc_final.api_movies.movies.dto

import com.tbc_final.api_movies.movies.entity.Genre

data class GenreDTO(
    val id: Int,
    val name: String
)

fun Genre.toDto(): GenreDTO = GenreDTO(id, name)