package com.tbc_final.api_movies.booking.util

data class BookingRequest(
    val user: String,
    val seatNumbers: List<String>
)
