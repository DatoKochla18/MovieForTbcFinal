package com.tbc_final.api_movies.screeing.entity

import com.tbc_final.api_movies.movies.entity.Movie
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "Screening")
data class Screening(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "screening_price")
    val screeningPrice: BigDecimal,
    @ManyToOne(fetch = FetchType.LAZY)
    val movie: Movie,

    val screeningTime: LocalDateTime,

    @Column(name = "icon_url")
    val iconUrl: String

)