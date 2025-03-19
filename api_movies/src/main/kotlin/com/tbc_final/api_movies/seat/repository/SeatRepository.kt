package com.tbc_final.api_movies.seat.repository

import com.tbc_final.api_movies.screeing.entity.Screening
import com.tbc_final.api_movies.seat.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository : JpaRepository<Seat, Long> {
    fun findByScreening(screening: Screening): List<Seat>
    fun findByScreeningAndSeatNumberIn(screening: Screening, seatNumbers: List<String>): List<Seat>
}