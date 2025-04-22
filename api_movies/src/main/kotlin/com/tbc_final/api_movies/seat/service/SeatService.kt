package com.tbc_final.api_movies.seat.service

import com.tbc_final.api_movies.booking.entity.Booking
import com.tbc_final.api_movies.booking.repository.BookingRepository
import com.tbc_final.api_movies.screeing.repository.ScreeningRepository
import com.tbc_final.api_movies.seat.repository.SeatRepository
import com.tbc_final.api_movies.seat.util.SeatStatus
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class SeatService(
    private val screeningRepository: ScreeningRepository,
    private val seatRepository: SeatRepository,
    private val bookingRepository: BookingRepository
) {

    private val logger = LoggerFactory.getLogger(SeatService::class.java)

    @Transactional
    fun updateSeatsStatus(
        screeningId: Int,
        seatNumbers: List<String>,
        newStatus: SeatStatus,
        userId: String? = null, // userId required for FREE -> HELD transition and for HELD->BOOKED
        discount: BigDecimal = BigDecimal.ZERO  // Added discount parameter with default value of 0
    ): Int {
        logger.info(
            "Updating seats status for screeningId: {}, seatNumbers: {}, newStatus: {}, userId: {}, discount: {}",
            screeningId, seatNumbers, newStatus, userId, discount
        )

        // Find the screening
        val screening = screeningRepository.findById(screeningId).orElseThrow {
            IllegalArgumentException("Screening not found for id: $screeningId")
        }

        // Find the seats to update
        val seatsToUpdate = seatRepository.findByScreeningAndSeatNumberIn(screening, seatNumbers)
        if (seatsToUpdate.size != seatNumbers.size) {
            val foundSeatNumbers = seatsToUpdate.map { it.seatNumber }
            val missingSeatNumbers = seatNumbers.filter { it !in foundSeatNumbers }
            logger.warn("Some seats not found. Missing seats: {}", missingSeatNumbers)
            throw IllegalArgumentException("The following seats do not exist: ${missingSeatNumbers.joinToString(", ")}")
        }

        // Process each seat based on its current status and the new status
        seatsToUpdate.forEach { seat ->
            logger.info("Processing seat: {} currentStatus: {}", seat.seatNumber, seat.status)
            when {
                seat.status == SeatStatus.FREE && newStatus == SeatStatus.BOOKED -> {
                    throw IllegalStateException("Direct transition from FREE to BOOKED is not allowed. Seat ${seat.seatNumber} must be HELD first.")
                }
                // FREE -> HELD: update seat and create a new booking record
                seat.status == SeatStatus.FREE && newStatus == SeatStatus.HELD -> {
                    logger.info("Transitioning seat {} from FREE to HELD", seat.seatNumber)
                    seat.status = newStatus
                    // Create new booking row (or update existing booking)
                    val existingBooking = bookingRepository.findByScreeningId(screeningId)
                        .find { it.user == userId && it.seatType == SeatStatus.HELD }
                    if (existingBooking == null) {
                        logger.info(
                            "No existing HELD booking found for user {}. Creating new booking for seat {}",
                            userId, seat.seatNumber
                        )
                        val booking = Booking(
                            screening = screening,
                            user = userId!!,
                            seatNumbers = seat.seatNumber,
                            seatType = SeatStatus.HELD,
                            inserted = LocalDateTime.now(),
                            discount = discount // Added discount
                        )
                        bookingRepository.save(booking)
                        logger.info("Created new booking: {}", booking)
                    } else {
                        logger.info("Found existing HELD booking for user {}: {}", userId, existingBooking)
                        // Append the seat if not already present.
                        val currentSeats = existingBooking.seatNumbers.split(",").map { it.trim() }
                        if (seat.seatNumber !in currentSeats) {
                            val updatedSeats = currentSeats.plus(seat.seatNumber).joinToString(",")
                            val updatedBooking = existingBooking.copy(
                                seatNumbers = updatedSeats,
                                discount = discount // Update discount
                            )
                            bookingRepository.save(updatedBooking)
                            logger.info(
                                "Updated booking with new seat {}. Updated booking: {}",
                                seat.seatNumber,
                                updatedBooking
                            )
                        } else {
                            logger.info("Seat {} is already in the booking", seat.seatNumber)
                        }
                    }
                }
                // HELD -> BOOKED: update seat and update the booking record accordingly
                seat.status == SeatStatus.HELD && newStatus == SeatStatus.BOOKED -> {
                    logger.info("Transitioning seat {} from HELD to BOOKED", seat.seatNumber)
                    seat.status = newStatus
                    // Look for booking in either HELD or BOOKED state
                    val booking = bookingRepository.findByScreeningId(screeningId)
                        .find { it.user.lowercase() == userId?.lowercase() && (it.seatType == SeatStatus.HELD || it.seatType == SeatStatus.BOOKED) }
                        ?: throw IllegalStateException("No booking found for user $userId")

                    // Combine current seats from the booking and the new seat (avoid duplicates)
                    val currentSeats = booking.seatNumbers.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    val updatedSeatNumbers = (currentSeats + seat.seatNumber).toSet().toList().sorted()
                    val updatedBooking = booking.copy(
                        seatNumbers = updatedSeatNumbers.joinToString(","),
                        seatType = SeatStatus.BOOKED,  // Ensure the booking is now marked as BOOKED
                        discount = discount // Update discount
                    )
                    bookingRepository.save(updatedBooking)
                    logger.info("Updated booking to BOOKED: {}", updatedBooking)
                }


                else -> {
                    logger.info(
                        "No specific transition rule for seat {}. Updating status to {}",
                        seat.seatNumber,
                        newStatus
                    )
                    seat.status = newStatus
                }
            }
        }

        // Save all updated seats
        seatRepository.saveAll(seatsToUpdate)
        if (newStatus == SeatStatus.FREE && userId != null) {
            // Find the booking for this screening and user
            val booking = bookingRepository.findByScreeningId(screeningId)
                .find { it.user.lowercase() == userId.lowercase() }
            booking?.let {
                val currentSeatNumbers = it.seatNumbers.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                logger.info("Current booking seats: {}", currentSeatNumbers)
                // Remove all seats that were freed (i.e. present in the request)
                val updatedSeats = currentSeatNumbers.filter { seatNum -> seatNum !in seatNumbers }
                logger.info("Updated seats after removing {}: {}", seatNumbers, updatedSeats)
                if (updatedSeats.isEmpty()) {
                    bookingRepository.delete(it)
                    logger.info("Deleted booking as no seats remain (freed seats: {})", seatNumbers)
                } else {
                    val updatedBooking = it.copy(
                        seatNumbers = updatedSeats.joinToString(","),
                        discount = discount // Update discount
                    )
                    bookingRepository.save(updatedBooking)
                    logger.info("Updated booking after removing seats {}: {}", seatNumbers, updatedBooking)
                }
            }
        }

        logger.info("Updated {} seats for screeningId: {}", seatsToUpdate.size, screeningId)
        return seatsToUpdate.size
    }
}