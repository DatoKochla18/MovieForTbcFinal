package com.tbc_final.api_movies.shopping.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_item")
data class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: ConcessionOrder? = null,  // Make nullable with default value

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val quantity: Int = 1,

    @Column(nullable = false)
    val unitPrice: BigDecimal,

    @Column(nullable = false)
    val subtotal: BigDecimal
)