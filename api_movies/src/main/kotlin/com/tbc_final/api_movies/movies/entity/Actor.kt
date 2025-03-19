package com.tbc_final.api_movies.movies.entity

import jakarta.persistence.*

@Entity
@Table(name = "actor")
data class Actor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,

    @Column(columnDefinition = "NVARCHAR(MAX)")
    val bio: String? = null
)