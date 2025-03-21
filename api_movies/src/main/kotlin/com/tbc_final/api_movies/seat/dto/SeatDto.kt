package com.tbc_final.api_movies.seat.dto

import com.tbc_final.api_movies.seat.entity.Seat
import com.tbc_final.api_movies.seat.util.SeatStatus
import java.math.BigDecimal

data class SeatDTO(
    val id: Int,
    val seatNumber: String,
    val status: SeatStatus,
    val vipAddOn: BigDecimal
)

fun Seat.toDTO() = SeatDTO(
    id = this.id,
    seatNumber = this.seatNumber,
    status = this.status,
    vipAddOn = this.vipAddOn
)