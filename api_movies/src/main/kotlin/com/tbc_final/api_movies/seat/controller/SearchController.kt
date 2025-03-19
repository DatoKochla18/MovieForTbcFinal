package com.tbc_final.api_movies.seat.controller

import com.tbc_final.api_movies.seat.dto.SeatStatusUpdateRequest
import com.tbc_final.api_movies.seat.service.SeatService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class SeatController(private val seatService: SeatService) {

    @PutMapping("/screenings/{screeningId}/seats/status")
    @ResponseStatus(HttpStatus.OK)
    fun updateSeatsStatus(
        @PathVariable screeningId: Int,
        @RequestBody statusUpdateRequest: SeatStatusUpdateRequest
    ): Map<String, Any> {
        val updatedCount = seatService.updateSeatsStatus(
            screeningId,
            statusUpdateRequest.seatNumbers,
            statusUpdateRequest.status
        )

        return mapOf(
            "screeningId" to screeningId,
            "updatedSeats" to statusUpdateRequest.seatNumbers,
            "newStatus" to statusUpdateRequest.status,
            "updatedCount" to updatedCount
        )
    }
}