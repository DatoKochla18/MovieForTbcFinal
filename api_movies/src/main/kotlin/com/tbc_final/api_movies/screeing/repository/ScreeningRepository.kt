package com.tbc_final.api_movies.screeing.repository

import com.tbc_final.api_movies.movies.entity.Movie
import com.tbc_final.api_movies.screeing.entity.Screening
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ScreeningRepository : JpaRepository<Screening, Int> {
    // Find screenings for a specific movie
    fun findByMovie(movie: Movie): List<Screening>

    // Find screenings between two dates
    fun findByScreeningTimeBetween(startTime: LocalDateTime, endTime: LocalDateTime): List<Screening>

    // Find screenings for movies with time range
    @Query("SELECT DISTINCT s.movie FROM Screening s WHERE s.screeningTime BETWEEN :startTime AND :endTime")
    fun findMoviesWithScreeningsBetween(
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<Movie>

    // Find screenings for movies with genre and time range
    @Query("SELECT DISTINCT s.movie FROM Screening s JOIN s.movie m JOIN m.genres g WHERE g.id = :genreId AND s.screeningTime BETWEEN :startTime AND :endTime")
    fun findMoviesWithGenreAndScreeningsBetween(
        @Param("genreId") genreId: Int,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<Movie>

    // Find screenings for movies with search term and time range
    @Query("SELECT DISTINCT s.movie FROM Screening s JOIN s.movie m WHERE (LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND s.screeningTime BETWEEN :startTime AND :endTime")
    fun findMoviesWithSearchTermAndScreeningsBetween(
        @Param("searchTerm") searchTerm: String,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<Movie>

    // Find screenings for movies with search term, genre, and time range
    @Query("SELECT DISTINCT s.movie FROM Screening s JOIN s.movie m JOIN m.genres g WHERE (LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND g.id = :genreId AND s.screeningTime BETWEEN :startTime AND :endTime")
    fun findMoviesWithSearchTermAndGenreAndScreeningsBetween(
        @Param("searchTerm") searchTerm: String,
        @Param("genreId") genreId: Int,
        @Param("startTime") startTime: LocalDateTime,
        @Param("endTime") endTime: LocalDateTime
    ): List<Movie>
}