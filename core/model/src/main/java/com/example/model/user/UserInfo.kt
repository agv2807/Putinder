package com.example.model.user

data class UserInfo(
    val login: String,
    val password: String,
    val name: String,
    val image: String = ""
)
