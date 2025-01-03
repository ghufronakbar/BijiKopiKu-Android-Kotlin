package com.example.bijikopiku.model.response

data class Order(
    val id: String,
    val userId: String,
    val paymentProof: String?,
    val status: String,
    val total: Int,
    val createdAt: String,
    val updatedAt: String,
    val orderItems: List<OrderItem>
)

data class OrderItem(
    val id: String,
    val orderId: String,
    val coffeeId: String,
    val quantity: Int,
    val total: Int,
    val createdAt: String,
    val updatedAt: String,
    val coffee: Coffee
)


