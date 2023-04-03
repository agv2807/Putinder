package com.example.model.chat

import com.example.model.user.ProfileResponse

data class MessageResponse(
    val message: String,
    val date: String,
    val user: ProfileResponse
)
