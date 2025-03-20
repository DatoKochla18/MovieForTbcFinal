package com.tbc_final.api_movies.movies.entity

import jakarta.persistence.*

@Entity
@Table(name = "genre")
data class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String
)