package com.tbc_final.api_movies.movies.controller

import com.tbc_final.api_movies.movies.dto.GenreDTO
import com.tbc_final.api_movies.movies.dto.MovieDTO
import com.tbc_final.api_movies.movies.dto.MovieDetailDTO
import com.tbc_final.api_movies.movies.service.MovieService
import com.tbc_final.api_movies.screeing.dto.ScreeningDTO
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/movies")
class MovieController(private val movieService: MovieService) {

    // Unified endpoint for listing all movies with optional search and filtering
    @GetMapping
    fun getMovies(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) genreId: Int?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: LocalDateTime?
    ): List<MovieDTO> = movieService.searchMovies(search, genreId, startTime, endTime)

    // Get movie details by ID
    @GetMapping("/{id}")
    fun getMovieDetails(@PathVariable id: Int): MovieDetailDTO {
        return movieService.getMovieById(id)
    }

    // Get all genres
    @GetMapping("/genres")
    fun getAllGenres(): List<GenreDTO> = movieService.getAllGenres()

    // Get screenings for a specific movie
    @GetMapping("/{id}/screenings")
    fun getScreeningsForMovie(@PathVariable id: Int): List<ScreeningDTO> {
        return movieService.getScreeningsForMovie(id)
    }

    // Get all screenings within a time range
    @GetMapping("/screenings")
    fun getScreenings(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: LocalDateTime
    ): List<ScreeningDTO> {
        return movieService.getScreeningsInTimeRange(startTime, endTime)
    }
}