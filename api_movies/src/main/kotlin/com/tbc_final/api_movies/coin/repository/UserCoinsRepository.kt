package com.tbc_final.api_movies.coin.repository

import com.tbc_final.api_movies.coin.entity.UserCoins
import org.springframework.data.repository.CrudRepository

interface UserCoinsRepository : CrudRepository<UserCoins, String> {
    fun findByUserUid(userUid: String): UserCoins?
}
