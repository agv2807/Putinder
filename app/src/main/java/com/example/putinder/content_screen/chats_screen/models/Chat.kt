package com.example.putinder.content_screen.chats_screen.models

import com.example.putinder.content_screen.profile_screen.models.ProfileResponse

data class Chat(
    val id: String,
    var lastMessage: LastMessage,
    val users: List<User>
)

data class User(
    val id: String,
    val name: String,
    val login: String,
    val image: String = ""
)

data class LastMessage(
    val date: String,
    val id: String,
    val message: String,
    val chatId: String,
    val user: ProfileResponse
)
