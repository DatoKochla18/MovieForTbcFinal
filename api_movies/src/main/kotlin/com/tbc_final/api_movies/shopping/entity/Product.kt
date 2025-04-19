package com.tbc_final.api_movies.shopping.entity

import jakarta.persistence.*
import java.math.BigDecimal

// Product.kt
@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val category: String,

    val imageUrl: String? = null,

    val isAvailable: Boolean = true
)