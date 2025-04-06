package com.tbc_final.api_movies.screeing.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class ScreeningDTO(
    val id: Int,
    val screeningTime: LocalDateTime,
    val screeningPrice: BigDecimal,
    val iconUrl:String
)

