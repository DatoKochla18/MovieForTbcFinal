package com.tbc_final.api_movies.booking.repository

import com.tbc_final.api_movies.booking.entity.Booking
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface BookingRepository : JpaRepository<Booking, Int> {
    fun findByScreeningId(screeningId: Int): List<Booking>
    fun findByUser(user: String): List<Booking>

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.id = :bookingId")
    fun deleteByBookingId(@Param("bookingId") bookingId: Int)
}