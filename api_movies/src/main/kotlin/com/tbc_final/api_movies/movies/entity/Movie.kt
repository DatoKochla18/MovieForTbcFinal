package com.tbc_final.api_movies.movies.entity

import jakarta.persistence.*

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val title: String,
    val description: String,
    val duration: Int,

    @ManyToMany
    @JoinTable(
        name = "movie_actor",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    val actors: Set<Actor> = emptySet()
)