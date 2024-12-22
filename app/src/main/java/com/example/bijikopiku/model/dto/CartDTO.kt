package com.example.bijikopiku.model.dto

data class CartDTO(
    val carts: List<CartItem>
)

data class CartItem(
    val id: String,
    val quantity: Int
)
