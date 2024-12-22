package com.example.bijikopiku.model.response

data class ApiRes<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
