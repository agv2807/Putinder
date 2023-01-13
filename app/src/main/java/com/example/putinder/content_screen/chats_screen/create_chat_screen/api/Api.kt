package com.example.putinder.content_screen.chats_screen.create_chat_screen.api

import com.example.putinder.content_screen.chats_screen.create_chat_screen.models.NewChatResponse
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
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