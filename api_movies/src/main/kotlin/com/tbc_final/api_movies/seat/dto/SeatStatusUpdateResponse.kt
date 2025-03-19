package com.tbc_final.api_movies.seat.dto

import com.tbc_final.api_movies.seat.util.SeatStatus
import java.time.LocalDateTime

data class SeatStatusUpdateResponse(
    val screeningId: Long,
    val updatedSeats: List<String>,
    val newStatus: SeatStatus,
    val updatedCount: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)