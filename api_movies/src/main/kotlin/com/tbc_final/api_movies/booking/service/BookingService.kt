package com.tbc_final.api_movies.booking.service

import com.tbc_final.api_movies.booking.entity.Booking
import com.tbc_final.api_movies.booking.repository.BookingRepository
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.repository.SeatRepository
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BookingService(
    private val screeningRepository: ScreeningRepository,
    private val seatRepository: SeatRepository,
    private val bookingRepository: BookingRepository
) {

    @Transactional
    fun bookSeats(screeningId: Long, user: String, seatNumbers: List<String>): Booking {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { IllegalArgumentException("Screening not found") }
        val seatsToBook = seatRepository.findByScreeningAndSeatNumberIn(screening, seatNumbers)

        seatsToBook.forEach { seat ->
            if (seat.status != SeatStatus.FREE) {
                throw IllegalStateException("Seat ${seat.seatNumber} is not available")
            }
            seat.status = SeatStatus.BOOKED
            seatRepository.save(seat)
        }

        // Convert the list to a comma-separated string
        val seatNumbersString = seatNumbers.joinToString(",")

        val booking = Booking(screening = screening, user = user, seatNumbers = seatNumbersString)
        return bookingRepository.save(booking)
    }
}