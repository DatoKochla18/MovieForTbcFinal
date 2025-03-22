package com.tbc_final.api_movies.seat.controller

import com.tbc_final.api_movies.booking.service.BookingService
import com.tbc_final.api_movies.booking.util.TicketSummary
import com.tbc_final.api_movies.seat.dto.SeatStatusUpdateRequest
import com.tbc_final.api_movies.seat.service.SeatService
import com.tbc_final.api_movies.seat.util.SeatStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class SeatController(
    private val seatService: SeatService,
    private val bookingService: BookingService
) {

    @PutMapping("/screening/{screeningId}/seats")
    @ResponseStatus(HttpStatus.OK)
    fun updateSeatsStatus(
        @PathVariable screeningId: Int,
        @RequestBody statusUpdateRequest: SeatStatusUpdateRequest
    ): Map<String, Any> {
        val updatedCount = seatService.updateSeatsStatus(
            screeningId,
            statusUpdateRequest.seatNumbers,
            statusUpdateRequest.status,
            statusUpdateRequest.userId
        )
        return mapOf(
            "screeningId" to screeningId,
            "updatedSeats" to statusUpdateRequest.seatNumbers,
            "newStatus" to statusUpdateRequest.status,
            "updatedCount" to updatedCount
        )
    }

    // Endpoint to get tickets by status
    @GetMapping("/tickets")
    fun getTicketsBySeatStatus(
        @RequestParam userId: String,
        @RequestParam status: SeatStatus
    ): TicketSummary {
        return bookingService.getTicketsBySeatStatus(userId, status)
    }
}
