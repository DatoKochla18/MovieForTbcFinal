package com.tbc_final.api_movies.booking.controller

import com.tbc_final.api_movies.booking.service.BookingService
import com.tbc_final.api_movies.booking.util.BookingRequest
import com.tbc_final.api_movies.booking.util.BookingResponse
import com.tbc_final.api_movies.booking.util.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BookingController(private val bookingService: BookingService) {

    @PostMapping("/{id}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    fun bookSeats(
        @PathVariable id: Long,
        @RequestBody bookingRequest: BookingRequest
    ): BookingResponse {
        val booking = bookingService.bookSeats(id, bookingRequest.user, bookingRequest.seatNumbers)
        return booking.toResponse()
    }
}