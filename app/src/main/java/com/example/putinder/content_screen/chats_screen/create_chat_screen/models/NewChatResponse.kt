package com.example.putinder.content_screen.chats_screen.create_chat_screen.models

import com.example.putinder.content_screen.chats_screen.models.User

data class NewChatResponse(
    val id: String,
    val users: List<User>
)
