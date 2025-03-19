package com.tbc_final.api_movies.movies.controller

import com.tbc_final.api_movies.movies.dto.MovieDTO
import com.tbc_final.api_movies.movies.dto.MovieDetailDTO
import com.tbc_final.api_movies.movies.service.MovieService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(private val movieService: MovieService) {

    // List all movies
    @GetMapping
    fun getMovies(): List<MovieDTO> = movieService.getAllMovies()

    @GetMapping("/{id}")
    fun getMovieDetails(@PathVariable id: Int): MovieDetailDTO {
        return movieService.getMovieById(id)
    }
}