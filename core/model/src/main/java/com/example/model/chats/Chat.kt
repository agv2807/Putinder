package com.example.model.chats

import com.example.model.user.ProfileResponse

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
