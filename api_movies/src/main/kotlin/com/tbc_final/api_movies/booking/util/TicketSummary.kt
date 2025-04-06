package com.tbc_final.api_movies.booking.util

import com.tbc_final.api_movies.seat.util.SeatStatus
import java.math.BigDecimal
import java.time.LocalDateTime

//data class TicketSummary(
//    val bookings: List<TicketDTO>,
//    val totalMoney: BigDecimal
//)
//
//data class TicketDTO(
//    val bookingId: Int,
//    val movieTitle: String,
//    val movieImgUrl: String,
//    val screeningTime: LocalDateTime,
//    val seatNumbers: String,
//    val seatType: SeatStatus
//)

data class TicketSummary(
    val tickets: List<TicketDto>
)

data class TicketDto(
    val bookingId: Int,
    val screeningId: Int,
    val movieTitle: String,
    val movieImgUrl: String,
    val screeningTime: LocalDateTime,
    val seatNumbers: String,
    val seatType: SeatStatus,
    val totalMoney: BigDecimal,
    val inserted: LocalDateTime
)
