package com.tbc_final.api_movies.booking.service

import com.tbc_final.api_movies.booking.entity.Booking
import com.tbc_final.api_movies.booking.repository.BookingRepository
import com.tbc_final.api_movies.booking.util.TicketDto
import com.tbc_final.api_movies.booking.util.TicketSummary
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.repository.SeatRepository
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

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
            if ((seat.status == SeatStatus.HELD)) {
                seat.status = SeatStatus.BOOKED
                seatRepository.save(seat)

            } else {
                throw IllegalStateException("Seat ${seat.seatNumber} is not available")

            }
        }

        val seatNumbersString = seatNumbers.joinToString(",")

        val booking =
            Booking(screening = screening, user = user, seatNumbers = seatNumbersString, inserted = LocalDateTime.now())
        return bookingRepository.save(booking)
    }

    fun getTicketsBySeatStatus(user: String, status: SeatStatus, orderType: String): TicketSummary {
        val bookings = bookingRepository.findByUser(user).filter { it.seatType == status }

        // Calculate total money
        val totalMoney = bookings.fold(BigDecimal.ZERO) { acc, booking ->
            val seatCount = booking.seatNumbers.split(",").filter { it.isNotBlank() }.size
            acc + booking.screening.screeningPrice.multiply(BigDecimal(seatCount))
        }

        // Map each Booking to a TicketDTO
        val ticketDTOs = bookings.map { booking ->
            val screening = booking.screening
            val movie = screening.movie
            TicketDto(
                bookingId = booking.id,
                screeningId = booking.screening.id,
                movieTitle = movie.title,
                movieImgUrl = movie.movieImgUrl,
                screeningTime = screening.screeningTime,
                seatNumbers = booking.seatNumbers,
                seatType = booking.seatType,
                totalMoney = totalMoney,
                inserted = booking.inserted
            )
        }
        return TicketSummary(if (orderType.lowercase() == "asc") ticketDTOs.sortedBy { it.inserted } else ticketDTOs.sortedByDescending { it.inserted })
    }
    fun deleteBookingById(bookingId: Int) {
        bookingRepository.deleteById(bookingId)

    }
}