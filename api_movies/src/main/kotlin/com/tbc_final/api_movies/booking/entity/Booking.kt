package com.tbc_final.api_movies.booking.entity

import com.tbc_final.api_movies.screeing.entity.Screening
import jakarta.persistence.*

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

    val seatNumbers: String
)