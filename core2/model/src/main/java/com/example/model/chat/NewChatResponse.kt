package com.example.model.chat

import com.example.putinder.content_screen.chats_screen.models.User

data class NewChatResponse(
    val id: String,
    val users: List<User>
)
