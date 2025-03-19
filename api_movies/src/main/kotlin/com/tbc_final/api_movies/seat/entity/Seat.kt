package com.tbc_final.api_movies.seat.entity

import com.tbc_final.api_movies.screeing.entity.Screening
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.persistence.*

@Entity
@Table(name = "Seat")
data class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val screening: Screening,

    val seatNumber: String,

    @Enumerated(EnumType.STRING)
    var status: SeatStatus = SeatStatus.FREE
)