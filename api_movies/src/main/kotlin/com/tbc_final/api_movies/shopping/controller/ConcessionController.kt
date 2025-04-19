package com.tbc_final.api_movies.shopping.controller

import com.tbc_final.api_movies.shopping.entity.Product
import com.tbc_final.api_movies.shopping.service.ConcessionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/concessions")
class ConcessionController(private val concessionService: ConcessionService) {

    // Get all products
    @GetMapping("/products")
    fun getAllProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(concessionService.getAllProducts())
    }

    // Get products by category
    @GetMapping("/products/category/{category}")
    fun getProductsByCategory(@PathVariable category: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(concessionService.getProductsByCategory(category))
    }

    // Get a specific product
    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: Long): ResponseEntity<Product> {
        val product = concessionService.getProduct(id)
        return ResponseEntity.ok(product)
    }

    // Create a new order
    @PostMapping("/orders")
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        val order = concessionService.createOrder(orderRequest.userUid, orderRequest.items)

        return ResponseEntity.ok(
            OrderResponse(
                orderId = order.id,
                trackingCode = order.trackingCode,
                totalAmount = order.totalAmount,
                status = order.status,
                items = order.items.map {
                    OrderItemResponse(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        subtotal = it.subtotal,
                        imgUrl = it.product.imageUrl ?: ""

                    )
                },
                createdAt = order.createdAt
            )
        )
    }

    // Get order by tracking code (for cashiers)
    @GetMapping("/orders/tracking/{code}")
    fun getOrderByTrackingCode(@PathVariable code: String): ResponseEntity<OrderResponse> {
        val order = concessionService.getOrderByTrackingCode(code)

        return ResponseEntity.ok(
            OrderResponse(
                orderId = order.id,
                trackingCode = order.trackingCode,
                totalAmount = order.totalAmount,
                status = order.status,
                items = order.items.map {
                    OrderItemResponse(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        subtotal = it.subtotal,
                        imgUrl = it.product.imageUrl ?: ""

                    )
                },
                createdAt = order.createdAt
            )
        )
    }

    // Get user's orders
    @GetMapping("/orders/user/{userUid}")
    fun getUserOrders(@PathVariable userUid: String): ResponseEntity<List<OrderResponse>> {
        val orders = concessionService.getUserOrders(userUid)

        return ResponseEntity.ok(
            orders.map { order ->
                OrderResponse(
                    orderId = order.id,
                    trackingCode = order.trackingCode,
                    totalAmount = order.totalAmount,
                    status = order.status,
                    items = order.items.map {
                        OrderItemResponse(
                            productId = it.product.id,
                            productName = it.product.name,
                            quantity = it.quantity,
                            unitPrice = it.unitPrice,
                            subtotal = it.subtotal,
                            imgUrl = it.product.imageUrl ?: ""

                        )
                    },
                    createdAt = order.createdAt
                )
            }.sortedByDescending { it.createdAt }
        )
    }

    // Update order status (e.g., for cashiers to mark as PAID or FULFILLED)
    @PutMapping("/orders/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestBody statusRequest: StatusUpdateRequest
    ): ResponseEntity<OrderResponse> {
        val order = concessionService.updateOrderStatus(id, statusRequest.status)

        return ResponseEntity.ok(
            OrderResponse(
                orderId = order.id,
                trackingCode = order.trackingCode,
                totalAmount = order.totalAmount,
                status = order.status,
                items = order.items.map {
                    OrderItemResponse(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        subtotal = it.subtotal,
                        imgUrl = it.product.imageUrl ?: ""
                    )
                },
                createdAt = order.createdAt
            )
        )
    }
}

// Request and response data classes
data class OrderRequest(
    val userUid: String,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val productId: Long,
    val quantity: Int
)

data class OrderResponse(
    val orderId: Int,
    val trackingCode: String,
    val totalAmount: BigDecimal,
    val status: String,
    val items: List<OrderItemResponse>,
    val createdAt: LocalDateTime
)

data class OrderItemResponse(
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal,
    val imgUrl: String
)

data class StatusUpdateRequest(
    val status: String
)