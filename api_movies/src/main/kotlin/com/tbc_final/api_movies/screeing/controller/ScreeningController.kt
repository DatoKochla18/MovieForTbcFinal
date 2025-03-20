package com.tbc_final.api_movies.screeing.controller

import com.tbc_final.api_movies.movies.service.MovieService
import com.tbc_final.api_movies.seat.dto.SeatDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ScreeningController(private val movieService: MovieService) {

    @GetMapping("/{id}/seats")
    fun getSeats(@PathVariable id: Int): List<SeatDTO> = movieService.getSeatsForScreening(id)
}