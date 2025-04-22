package com.tbc_final.api_movies.coin.controller

import com.tbc_final.api_movies.coin.entity.UserCoins
import com.tbc_final.api_movies.coin.service.UserCoinsService
import com.tbc_final.api_movies.coin.util.AddCoinsRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/coins")
class UserCoinsController(private val service: UserCoinsService) {

    @GetMapping("/{userUid}")
    fun getCoins(@PathVariable userUid: String): ResponseEntity<UserCoins> {
        val userCoins = service.getOrCreateUserCoins(userUid)
        return ResponseEntity.ok(userCoins)
    }

    @PostMapping("/{userUid}")
    fun addCoins(
        @PathVariable userUid: String,
        @RequestBody request: AddCoinsRequest
    ): ResponseEntity<UserCoins> {
        val userCoins = service.addCoins(userUid, request.amount)
        return ResponseEntity.ok(userCoins)
    }
}