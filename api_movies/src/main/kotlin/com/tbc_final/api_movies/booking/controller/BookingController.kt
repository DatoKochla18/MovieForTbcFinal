package com.tbc_final.api_movies.booking.controller

import com.tbc_final.api_movies.booking.service.BookingService
import com.tbc_final.api_movies.booking.util.BookingRequest
import com.tbc_final.api_movies.booking.util.BookingResponse
import com.tbc_final.api_movies.booking.util.toResponse
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
    fun deleteBooking(@PathVariable id: Int): Map<String, Boolean> {
        return try {
            val deleted = bookingService.deleteBookingById(id)
            mapOf("deleted" to deleted)
        } catch (ex: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete booking: ${ex.message}")
        }
    }

    @DeleteMapping("/bookings")
    fun deleteMultipleBookings(@RequestBody bookingIds: List<Int>): Map<String, Boolean> {
        return try {
            bookingService.deleteMultipleBookings(bookingIds)
            mapOf("deleted" to true)
        } catch (ex: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete bookings: ${ex.message}")
        }
    }


//    @GetMapping("/bookings")
//    fun getBookingsByUser(@RequestParam userId: String): List<BookMovieResponse> {
//        val bookings = bookingService.getBookingsByUser(userId)
//        return bookings.map { it.toBookMovieResponse() }
//    }
}
