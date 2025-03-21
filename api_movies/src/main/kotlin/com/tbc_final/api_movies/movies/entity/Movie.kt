package com.tbc_final.api_movies.movies.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val title: String,
    val description: String,
    val duration: Int,
    @Column(name = "imdb_rating")
    val imdbRating: BigDecimal,
    val director: String,
    @Column(name = "age_restriction")
    val ageRestriction: String,
    @Column(name = "movie_img_url")
    val movieImgUrl: String,
    @ManyToMany
    @JoinTable(
        name = "movie_actor",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    val actors: Set<Actor> = emptySet(),

    @ManyToMany
    @JoinTable(
        name = "movie_genre",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: Set<Genre> = emptySet()
)