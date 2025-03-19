package com.tbc_final.api_movies.booking.util

import com.tbc_final.api_movies.booking.entity.Booking
import java.time.LocalDateTime

data class ScreeningResponse(
    val id: Int,
    val movieTitle: String,
    val screeningTime: LocalDateTime
)

data class BookingResponse(
    val id: Int,
    val screening: ScreeningResponse,
    val user: String,
    val seatNumbers: List<String>
)

fun Booking.toResponse(): BookingResponse {
    return BookingResponse(
        id = this.id,
        screening = ScreeningResponse(
            id = this.screening.id,
            movieTitle = this.screening.movie.title,
            screeningTime = this.screening.screeningTime
        ),
        user = this.user,
        seatNumbers = this.seatNumbers.split(",")
    )
}