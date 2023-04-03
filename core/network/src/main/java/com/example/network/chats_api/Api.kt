package com.example.network.chats_api

import com.example.model.chats.Chat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface Api {

    @GET("chats")
    fun getChatsResponse(
        @Header("Authorization") token: String?
    ): Call<List<Chat>>

}