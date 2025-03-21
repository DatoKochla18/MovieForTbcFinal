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
    fun bookSeats(screeningId: Int, user: String, seatNumbers: List<String>): Booking {
        val screening = screeningRepository.findById(screeningId)
            .orElseThrow { IllegalArgumentException("Screening not found") }

        val seatsToBook = seatRepository.findByScreeningAndSeatNumberIn(screening, seatNumbers)
        if (seatsToBook.size != seatNumbers.size) {
            val foundSeatNumbers = seatsToBook.map { it.seatNumber }
            val missingSeatNumbers = seatNumbers.filter { it !in foundSeatNumbers }

            throw IllegalArgumentException("The following seats do not exist: ${missingSeatNumbers.joinToString(", ")}")
        }

        seatsToBook.forEach { seat ->
            if ( (seat.status == SeatStatus.HELD)) {
                seat.status = SeatStatus.BOOKED
                seatRepository.save(seat)

            } else {
                throw IllegalStateException("Seat ${seat.seatNumber} is not available")

            }
        }

        val seatNumbersString = seatNumbers.joinToString(",")

        val booking = Booking(screening = screening, user = user, seatNumbers = seatNumbersString)
        return bookingRepository.save(booking)
    }

    fun getBookingsByUser(user: String): List<Booking> {
        return bookingRepository.findByUser(user)
    }
}