package com.example.putinder.content_screen.chats_screen.api

import com.example.putinder.content_screen.chats_screen.models.Chat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface Api {

    @GET("chats")
    fun getChatsResponse(
        @Header("Authorization") token: String?
    ): Call<List<Chat>>

}