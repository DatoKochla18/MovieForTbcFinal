package com.tbc_final.api_movies.screeing.repository

import com.tbc_final.api_movies.movies.entity.Movie
import com.tbc_final.api_movies.screeing.entity.Screening
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScreeningRepository : JpaRepository<Screening, Long> {
    fun findByMovie(movie: Movie): List<Screening>
}