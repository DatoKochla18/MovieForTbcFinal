package com.tbc_final.api_movies.shopping.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

// ConcessionOrder.kt
@Entity
@Table(name = "concession_order")
data class ConcessionOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val trackingCode: String,

    @Column(nullable = false)
    val userUid: String,

    @Column(nullable = false)
    val totalAmount: BigDecimal,

    @Column(nullable = false)
    val status: String = "PENDING",

    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf()

    // Helper method to add items and maintain the bidirectional relationship
    fun addItem(item: OrderItem) {
        items.add(item)
        item.order = this  // Now we can assign to the var property
    }
}