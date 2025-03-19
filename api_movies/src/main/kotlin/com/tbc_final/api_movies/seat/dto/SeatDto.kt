package com.tbc_final.api_movies.seat.dto

import com.tbc_final.api_movies.seat.entity.Seat
import com.tbc_final.api_movies.seat.util.SeatStatus

data class SeatDTO(
    val id: Int,
    val seatNumber: String,
    val status: SeatStatus
)

fun Seat.toDTO() = SeatDTO(
    id = this.id,
    seatNumber = this.seatNumber,
    status = this.status
)