package com.example.bijikopiku.model.response

data class Coffee(
    val id: String,
    val name: String,
    val price: Long,
    val isForCoffeeEnthusiast: Boolean,
    val type: String,
    val taste: String,
    val isItForSweet: Boolean,
    val flavor: String,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String,
    var quantity: Int,
    val sold: Int,
    val picture: String?,
    val desc: String,
    val percentageMatchRate: Double?
)
