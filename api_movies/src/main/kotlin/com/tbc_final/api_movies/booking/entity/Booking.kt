package com.tbc_final.api_movies.booking.entity

import com.tbc_final.api_movies.screeing.entity.Screening
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "booking")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val screening: Screening,
    @Column(name = "user_uid")
    val user: String,

    val seatNumbers: String,
    @Enumerated(EnumType.STRING)
    var seatType: SeatStatus = SeatStatus.HELD,

    @Column(name = "inserted")
    val inserted: LocalDateTime,


    @Column(name = "discount", columnDefinition = "decimal(10,2)")
    val discount: BigDecimal = BigDecimal.ZERO
)

