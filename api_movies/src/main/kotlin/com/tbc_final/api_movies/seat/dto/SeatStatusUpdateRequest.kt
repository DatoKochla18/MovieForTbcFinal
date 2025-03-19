package com.tbc_final.api_movies.seat.dto

import com.tbc_final.api_movies.seat.util.SeatStatus

data class SeatStatusUpdateRequest(
    val seatNumbers: List<String>,
    val status: SeatStatus
)