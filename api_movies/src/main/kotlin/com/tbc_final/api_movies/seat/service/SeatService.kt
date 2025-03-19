package com.tbc_final.api_movies.seat.service

import com.tbc_final.api_movies.booking.repository.BookingRepository
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.repository.SeatRepository
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SeatService(
    private val screeningRepository: ScreeningRepository,
    private val seatRepository: SeatRepository,
    private val bookingRepository: BookingRepository
) {

    @Transactional
    fun updateSeatsStatus(screeningId: Int, seatNumbers: List<String>, newStatus: SeatStatus): Int {
        // Find the screening
        val screening = screeningRepository.findById(screeningId).orElseThrow()

        // Find the seats to update
        val seatsToUpdate = seatRepository.findByScreeningAndSeatNumberIn(screening, seatNumbers)

        // Validate that all requested seats exist
        if (seatsToUpdate.size != seatNumbers.size) {
            val foundSeatNumbers = seatsToUpdate.map { it.seatNumber }
            val missingSeatNumbers = seatNumbers.filter { it !in foundSeatNumbers }

            throw IllegalArgumentException("The following seats do not exist: ${missingSeatNumbers.joinToString(", ")}")
        }

        // Track which seats were previously booked
        val previouslyBookedSeats = seatsToUpdate.filter { it.status == SeatStatus.BOOKED }.map { it.seatNumber }

        // If we have seats that were previously booked and we're changing them
        if (previouslyBookedSeats.isNotEmpty() && newStatus != SeatStatus.BOOKED) {
            // Find bookings containing these seats
            val bookings = bookingRepository.findByScreeningId(screeningId)

            // Process each booking to remove the affected seats
            bookings.forEach { booking ->
                val bookedSeats = booking.seatNumbers.split(",")
                val remainingSeats = bookedSeats.filter { seatNumber ->
                    seatNumber !in previouslyBookedSeats
                }

                if (remainingSeats.isEmpty()) {
                    // If no seats left in booking, delete the entire booking
                    bookingRepository.delete(booking)
                } else if (remainingSeats.size < bookedSeats.size) {
                    // Update booking with remaining seats
                    val updatedBooking = booking.copy(seatNumbers = remainingSeats.joinToString(","))
                    bookingRepository.save(updatedBooking)
                }
                // If no seats in this booking are affected, do nothing
            }
        }

        // Update seat statuses
        seatsToUpdate.forEach { seat ->
            seat.status = newStatus
        }

        // Save all updated seats
        seatRepository.saveAll(seatsToUpdate)

        return seatsToUpdate.size
    }
}