package com.tbc_final.api_movies.seat.entity

import com.tbc_final.api_movies.screeing.entity.Screening
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "Seat")
data class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val screening: Screening,

    @Column(name = "seat_number")
    val seatNumber: String,


    @Column(name = "img_url")
    val imgUrl: String,

    @Column(name = "vip_add_on")
    val vipAddOn: BigDecimal,
    @Enumerated(EnumType.STRING)
    var status: SeatStatus = SeatStatus.FREE
)