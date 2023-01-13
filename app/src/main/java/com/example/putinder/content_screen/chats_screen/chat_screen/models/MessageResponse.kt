package com.example.putinder.content_screen.chats_screen.chat_screen.models

import com.example.putinder.content_screen.profile_screen.models.ProfileResponse

data class MessageResponse(
    val message: String,
    val date: String,
    val user: ProfileResponse
)
