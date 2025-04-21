package com.tbc_final.api_movies.movies.repository

import com.tbc_final.api_movies.movies.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Int> {
    // Find movies containing a search term in title or description
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun findByTitleOrDescriptionContaining(@Param("searchTerm") searchTerm: String): List<Movie>

    // Find movies by genre ID
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    fun findByGenreId(@Param("genreId") genreId: Int): List<Movie>

    // Find movies by search term and genre
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE (LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND g.id = :genreId")
    fun findByTitleOrDescriptionContainingAndGenreId(
        @Param("searchTerm") searchTerm: String,
        @Param("genreId") genreId: Int
    ): List<Movie>
    fun findByIsUpcoming(isUpcoming: Boolean): List<Movie>
}