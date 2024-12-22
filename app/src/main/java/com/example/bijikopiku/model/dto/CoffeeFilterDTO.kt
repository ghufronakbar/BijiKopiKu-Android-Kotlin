package com.example.bijikopiku.model.dto

data class CoffeeFilterDTO(
    val isForCoffeeEnthusiast: Boolean,
    val type: String,
    val taste: String,
    val flavor: String,
    val isItForSweet: Boolean
)
