package com.example.bijikopiku.model

data class CoffeeCart(
    val id: String,
    val name: String,
    val price: Long,
    val type: String,
    val flavor: String,
    var amount: Int
)
