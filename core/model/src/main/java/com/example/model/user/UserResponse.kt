package com.example.model.user

data class UserResponse(
    val user: User,
    val token: String
)

data class User(
    val image: String,
    val name: String,
    val id: String,
    val login: String
)
