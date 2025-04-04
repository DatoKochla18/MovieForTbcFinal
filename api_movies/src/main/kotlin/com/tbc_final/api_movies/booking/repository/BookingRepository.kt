package com.tbc_final.api_movies.booking.repository

import com.tbc_final.api_movies.booking.entity.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface BookingRepository : JpaRepository<Booking, Int> {
    fun findByScreeningId(screeningId: Int): List<Booking>
    fun findByUser(user: String): List<Booking>
}