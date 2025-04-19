package com.tbc_final.api_movies.shopping.repository

import com.tbc_final.api_movies.shopping.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByIsAvailableTrue(): List<Product>
    fun findByCategoryAndIsAvailableTrue(category: String): List<Product>
}