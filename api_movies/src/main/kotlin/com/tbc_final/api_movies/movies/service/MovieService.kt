package com.tbc_final.api_movies.movies.service

import com.tbc_final.api_movies.movies.dto.*
import com.tbc_final.api_movies.movies.repository.GenreRepository
import com.tbc_final.api_movies.movies.repository.MovieRepository
import com.tbc_final.api_movies.screeing.dto.ScreeningDTO
import com.tbc_final.api_movies.screeing.dto.toDTO
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.dto.SeatDTO
import com.tbc_final.api_movies.seat.dto.toDTO
import com.tbc_final.api_movies.seat.repository.SeatRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository,
    private val screeningRepository: ScreeningRepository,
    private val seatRepository: SeatRepository
) {
    // Enhanced method for searching and filtering movies
    fun searchMovies(
        searchTerm: String?,
        genreId: Int?,
        startTime: LocalDateTime?,
        endTime: LocalDateTime?
    ): List<MovieDTO> {
        // If no time range is specified, use the standard repository methods
        if (startTime == null || endTime == null) {
            return when {
                searchTerm != null && genreId != null ->
                    movieRepository.findByTitleOrDescriptionContainingAndGenreId(searchTerm, genreId)
                        .map { it.toDTO() }

                searchTerm != null ->
                    movieRepository.findByTitleOrDescriptionContaining(searchTerm)
                        .map { it.toDTO() }

                genreId != null ->
                    movieRepository.findByGenreId(genreId)
                        .map { it.toDTO() }

                else -> movieRepository.findAll().map { it.toDTO() }
            }
        }

        // If time range is specified, use the screening repository methods
        return when {
            searchTerm != null && genreId != null ->
                screeningRepository.findMoviesWithSearchTermAndGenreAndScreeningsBetween(
                    searchTerm, genreId, startTime, endTime
                ).map { it.toDTO() }

            searchTerm != null ->
                screeningRepository.findMoviesWithSearchTermAndScreeningsBetween(
                    searchTerm, startTime, endTime
                ).map { it.toDTO() }

            genreId != null ->
                screeningRepository.findMoviesWithGenreAndScreeningsBetween(
                    genreId, startTime, endTime
                ).map { it.toDTO() }

            else ->
                screeningRepository.findMoviesWithScreeningsBetween(startTime, endTime)
                    .map { it.toDTO() }
        }
    }

    fun getMovieById(id: Int): MovieDetailDTO {
        return movieRepository.findById(id).map { it.toDetailDto() }
            .orElseThrow { ResourceNotFoundException("Movie not found with id: $id") }
    }

    fun getAllGenres(): List<GenreDTO> =
        genreRepository.findAll().map { GenreDTO(it.id, it.name) }

    // Get all screenings for a specific movie
    fun getScreeningsForMovie(movieId: Int): List<ScreeningDTO> {
        val movie = movieRepository.findById(movieId)
            .orElseThrow { ResourceNotFoundException("Movie not found with id: $movieId") }
        return screeningRepository.findByMovie(movie).map { it.toDTO() }
    }

    // Get screenings within a time range
    fun getScreeningsInTimeRange(startTime: LocalDateTime, endTime: LocalDateTime): List<ScreeningDTO> {
        return screeningRepository.findByScreeningTimeBetween(startTime, endTime).map { it.toDTO() }
    }

    fun getSeatsForScreening(screeningId: Int): List<SeatDTO> {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { ResourceNotFoundException("Screening not found with id: $screeningId") }
        return seatRepository.findByScreening(screening).map { it.toDTO() }
    }
}

class ResourceNotFoundException(message: String) : RuntimeException(message)