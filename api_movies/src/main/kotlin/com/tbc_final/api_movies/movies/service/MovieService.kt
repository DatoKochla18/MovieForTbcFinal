package com.tbc_final.api_movies.movies.service

import com.tbc_final.api_movies.movies.dto.MovieDTO
import com.tbc_final.api_movies.movies.dto.MovieDetailDTO
import com.tbc_final.api_movies.movies.dto.toDTO
import com.tbc_final.api_movies.movies.repository.MovieRepository
import com.tbc_final.api_movies.screeing.dto.toDTO
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.dto.SeatDTO
import com.tbc_final.api_movies.seat.dto.toDTO
import com.tbc_final.api_movies.seat.repository.SeatRepository
import org.springframework.stereotype.Service


@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val screeningRepository: ScreeningRepository,
    private val seatRepository: SeatRepository
) {

    fun getAllMovies(): List<MovieDTO> =
        movieRepository.findAll().map { it.toDTO() }

    fun getMovieDetail(movieId: Long): MovieDetailDTO {
        val movie = movieRepository.findById(movieId)
            .orElseThrow { IllegalArgumentException("Movie not found") }
        val screenings = screeningRepository.findByMovie(movie).map { it.toDTO() }
        return MovieDetailDTO(movie.toDTO(), screenings)
    }

    fun getSeatsForScreening(screeningId: Long): List<SeatDTO> {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { IllegalArgumentException("Screening not found") }
        return seatRepository.findByScreening(screening).map { it.toDTO() }
    }
}