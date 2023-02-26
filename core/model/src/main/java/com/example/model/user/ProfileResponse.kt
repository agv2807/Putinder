package com.example.model.user

data class ProfileResponse(
    val login: String,
    val id: String,
    var image: String,
    var name: String
)
