package com.tbc_final.api_movies.seat.dto

import com.tbc_final.api_movies.seat.util.SeatStatus
import java.math.BigDecimal

data class SeatStatusUpdateRequest(
    val seatNumbers: List<String>,
    val status: SeatStatus,
    val userId: String? = null,
    val discount: BigDecimal = BigDecimal.ZERO
)