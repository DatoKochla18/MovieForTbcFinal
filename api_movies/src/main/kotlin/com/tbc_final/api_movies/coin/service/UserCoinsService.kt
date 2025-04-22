package com.tbc_final.api_movies.coin.service

import com.tbc_final.api_movies.coin.entity.UserCoins
import com.tbc_final.api_movies.coin.repository.UserCoinsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserCoinsService(private val repository: UserCoinsRepository) {

    @Transactional
    fun getOrCreateUserCoins(userUid: String): UserCoins {
        return repository.findByUserUid(userUid) ?: repository.save(UserCoins(userUid, 0))
    }

    @Transactional
    fun addCoins(userUid: String, amount: Int): UserCoins {
        val userCoins = getOrCreateUserCoins(userUid)
        val updatedCoins = userCoins.copy(coins = userCoins.coins + amount)
        return repository.save(updatedCoins)
    }
}