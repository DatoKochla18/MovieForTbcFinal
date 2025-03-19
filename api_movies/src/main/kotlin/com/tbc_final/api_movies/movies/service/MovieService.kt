package com.tbc_final.api_movies.movies.service

import com.tbc_final.api_movies.movies.dto.MovieDTO
import com.tbc_final.api_movies.movies.dto.MovieDetailDTO
import com.tbc_final.api_movies.movies.dto.toDTO
import com.tbc_final.api_movies.movies.dto.toDetailDto
import com.tbc_final.api_movies.movies.repository.MovieRepository
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

    fun getMovieById(id: Int): MovieDetailDTO {
        return movieRepository.findById(id).map { it.toDetailDto() }
            .orElseThrow()
    }

    fun getSeatsForScreening(screeningId: Int): List<SeatDTO> {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { IllegalArgumentException("Screening not found") }
        return seatRepository.findByScreening(screening).map { it.toDTO() }
    }
}