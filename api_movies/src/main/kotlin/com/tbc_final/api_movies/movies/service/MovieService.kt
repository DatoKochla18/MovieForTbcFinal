package com.tbc_final.api_movies.movies.service

import com.tbc_final.api_movies.movies.dto.*
import com.tbc_final.api_movies.movies.repository.GenreRepository
import com.tbc_final.api_movies.movies.repository.MovieRepository
import com.tbc_final.api_movies.screeing.dto.ScreeningDTO
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
        // Retrieve the movie entity
        val movie = movieRepository.findById(id)
            .orElseThrow { RuntimeException("Movie not found") }

        // Fetch all screenings for the movie
        val screeningDTOs = screeningRepository.findByMovie(movie)
            .map { ScreeningDTO(id = it.id, screeningPrice = it.screeningPrice, screeningTime = it.screeningTime) }

        // Convert the movie entity to a DTO including screenings
        return movie.toDetailDto(screeningDTOs)
    }

    fun getAllGenres(): List<GenreDTO> =
        genreRepository.findAll().map { GenreDTO(it.id, it.name) }


    fun getSeatsForScreening(screeningId: Int): List<SeatDTO> {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { ResourceNotFoundException("Screening not found with id: $screeningId") }
        return seatRepository.findByScreening(screening).map { it.toDTO() }.sortedBy { it.seatNumber.lowercase() }
    }
}

class ResourceNotFoundException(message: String) : RuntimeException(message)