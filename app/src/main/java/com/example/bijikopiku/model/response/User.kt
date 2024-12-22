package com.example.bijikopiku.model.response

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val accessToken: String
)

