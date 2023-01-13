package com.example.putinder.content_screen.chats_screen.chat_screen.api

import com.example.putinder.content_screen.chats_screen.chat_screen.models.MessageResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("chats/{chatId}/messages")
    fun pushMessage(
        @Header("Authorization") token: String?,
        @Path("chatId") chatId: String,
        @Field("message") message: String
    ): Call<MessageResponse>

    @GET("chats/{chatId}/messages")
    fun getMessagesList(
        @Header("Authorization") token: String?,
        @Path("chatId") chatId: String
    ): Call<List<MessageResponse>>

}