package com.tbc_final.api_movies.coin.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_coins")
data class UserCoins(
    @Id val userUid: String,
    val coins: Int
)