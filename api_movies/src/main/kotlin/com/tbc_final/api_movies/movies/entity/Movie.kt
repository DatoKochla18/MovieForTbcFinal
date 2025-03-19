package com.tbc_final.api_movies.movies.entity

import jakarta.persistence.*

@Entity
@Table(name = "Movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val title: String,
    val description: String,
    val duration: Int
)