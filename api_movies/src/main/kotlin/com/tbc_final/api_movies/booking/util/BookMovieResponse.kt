package com.tbc_final.api_movies.booking.util

import com.tbc_final.api_movies.booking.entity.Booking

data class BookMovieResponse(
    val movieName: String,
    val seatNumbers: String
)


fun Booking.toBookMovieResponse(): BookMovieResponse {
    return BookMovieResponse(
        movieName = this.screening.movie.title, // Access the movie title
        seatNumbers = this.seatNumbers
    )
}