package com.example.network.create_chat_api

import com.example.model.chat.NewChatResponse
import com.example.model.user.ProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("chats")
    fun createNewChat(
        @Header("Authorization") token: String?,
        @Field("user_id") userId: String
    ): Call<NewChatResponse>

    @GET("users")
    fun getUsers(
        @Header("Authorization") token: String?,
    ): Call<List<ProfileResponse>>

}