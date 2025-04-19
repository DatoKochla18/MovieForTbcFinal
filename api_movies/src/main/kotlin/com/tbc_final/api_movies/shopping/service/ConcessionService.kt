package com.tbc_final.api_movies.shopping.service

import com.tbc_final.api_movies.shopping.controller.OrderItemRequest
import com.tbc_final.api_movies.shopping.entity.ConcessionOrder
import com.tbc_final.api_movies.shopping.entity.OrderItem
import com.tbc_final.api_movies.shopping.entity.Product
import com.tbc_final.api_movies.shopping.repository.ConcessionOrderRepository
import com.tbc_final.api_movies.shopping.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*


@Service
class ConcessionService(
    private val productRepository: ProductRepository,
    private val orderRepository: ConcessionOrderRepository
) {

    fun getAllProducts(): List<Product> {
        return productRepository.findByIsAvailableTrue()
    }

    fun getProductsByCategory(category: String): List<Product> {
        return productRepository.findByCategoryAndIsAvailableTrue(category)
    }

    fun getProduct(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { RuntimeException("Product not found with id: $id") }
    }

    @Transactional
    fun createOrder(userUid: String, items: List<OrderItemRequest>): ConcessionOrder {
        // Validate products exist and calculate total
        val productItems = items.map { itemRequest ->
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { RuntimeException("Product not found") }

            if (!product.isAvailable) {
                throw RuntimeException("Product ${product.name} is not available")
            }

            ProductItemData(
                product = product,
                quantity = itemRequest.quantity,
                unitPrice = product.price,
                subtotal = product.price.multiply(BigDecimal(itemRequest.quantity))
            )
        }

        // Calculate total amount
        val totalAmount = productItems.sumOf { it.subtotal }

        // Generate a random 6-digit tracking code
        val trackingCode = generateTrackingCode()

        // Create the order
        val order = ConcessionOrder(
            trackingCode = trackingCode,
            userUid = userUid,
            totalAmount = totalAmount
        )

        // Add items to the order
        productItems.forEach { productItem ->
            val orderItem = OrderItem(
                product = productItem.product,
                quantity = productItem.quantity,
                unitPrice = productItem.unitPrice,
                subtotal = productItem.subtotal
                // order parameter is omitted - we'll set it via the addItem method
            )
            order.addItem(orderItem)
        }

        // Save the order with its items in one transaction
        return orderRepository.save(order)
    }

    fun getOrderByTrackingCode(trackingCode: String): ConcessionOrder {
        return orderRepository.findByTrackingCode(trackingCode)
            .orElseThrow { RuntimeException("Order not found with tracking code: $trackingCode") }
    }

    fun getUserOrders(userUid: String): List<ConcessionOrder> {
        return orderRepository.findByUserUid(userUid)
    }

    @Transactional
    fun updateOrderStatus(id: Long, status: String): ConcessionOrder {
        val validStatuses = listOf("PENDING", "PAID", "FULFILLED", "CANCELLED")

        if (!validStatuses.contains(status)) {
            throw IllegalArgumentException("Invalid status: $status. Must be one of: ${validStatuses.joinToString()}")
        }

        val order = orderRepository.findById(id)
            .orElseThrow { RuntimeException("Order not found with id: $id") }

        // Create a new instance with the updated status since ConcessionOrder uses vals
        val updatedOrder = order.copy(status = status)

        // The items still reference the old order, so we need to reassign them
        updatedOrder.items.clear()
        order.items.forEach { item ->
            val newItem = OrderItem(
                product = item.product,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                subtotal = item.subtotal
            )
            updatedOrder.addItem(newItem)
        }

        return orderRepository.save(updatedOrder)
    }

    private fun generateTrackingCode(): String {
        val random = Random()
        var code: String
        var isUnique = false

        do {
            // Generate a 6-digit code
            code = String.format("%06d", random.nextInt(1000000))
            isUnique = !orderRepository.findByTrackingCode(code).isPresent
        } while (!isUnique)

        return code
    }
}

data class ProductItemData(
    val product: Product,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)

