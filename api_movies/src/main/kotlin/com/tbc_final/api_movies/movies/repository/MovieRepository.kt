package com.tbc_final.api_movies.movies.repository

import com.tbc_final.api_movies.movies.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Long>
