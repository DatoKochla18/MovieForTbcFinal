package com.tbc_final.api_movies.shopping.repository

import com.tbc_final.api_movies.shopping.entity.ConcessionOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConcessionOrderRepository : JpaRepository<ConcessionOrder, Long> {
    fun findByTrackingCode(trackingCode: String): Optional<ConcessionOrder>
    fun findByUserUid(userUid: String): List<ConcessionOrder>
}