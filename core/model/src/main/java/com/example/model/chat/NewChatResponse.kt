package com.example.model.chat

import com.example.model.chats.User

data class NewChatResponse(
    val id: String,
    val users: List<User>
)
