package com.example.bijikopiku.model.dto

data class CheckoutDTO(
    val orderItems: List<CheckoutItem>
)

data class CheckoutItem(
    val coffeeId: String,
    val quantity: Int
)