package com.example.putinder.sign_screen.models

data class UserInfo(
    val login: String,
    val password: String,
    val name: String,
    val image: String = ""
)
