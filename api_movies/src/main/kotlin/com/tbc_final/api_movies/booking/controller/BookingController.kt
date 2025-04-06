package com.tbc_final.api_movies.booking.controller

import com.tbc_final.api_movies.booking.service.BookingService
import com.tbc_final.api_movies.booking.util.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class BookingController(private val bookingService: BookingService) {

    // Endpoint to book seats for a specific screening
    @PostMapping("/{id}/book")
    @ResponseStatus(HttpStatus.CREATED)
    fun bookSeats(
        @PathVariable id: Int,
        @RequestBody bookingRequest: BookingRequest
    ): BookingResponse {
        try {
            val booking = bookingService.bookSeats(id, bookingRequest.user, bookingRequest.seatNumbers)
            return booking.toResponse()
        } catch (ex: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        } catch (ex: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, ex.message)
        }
    }
    @DeleteMapping("/booking/{id}")
    fun deleteBooking(@PathVariable id: Int) {
        try {
            bookingService.deleteBookingById(id)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")
        }
    }

//    @GetMapping("/bookings")
//    fun getBookingsByUser(@RequestParam userId: String): List<BookMovieResponse> {
//        val bookings = bookingService.getBookingsByUser(userId)
//        return bookings.map { it.toBookMovieResponse() }
//    }
}