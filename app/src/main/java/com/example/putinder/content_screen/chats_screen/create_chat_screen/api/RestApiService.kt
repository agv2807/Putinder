package com.example.putinder.content_screen.chats_screen.create_chat_screen.api

import com.example.putinder.content_screen.chats_screen.create_chat_screen.models.NewChatResponse
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.example.putinder.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun createNewChat(token: String?, userId: String, onResult: (NewChatResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.createNewChat("Bearer $token", userId).enqueue(
            object : Callback<NewChatResponse> {
                override fun onResponse(call: Call<NewChatResponse>, response: Response<NewChatResponse>) {
                    val chatResponse = response.body()
                    onResult(chatResponse)
                }

                override fun onFailure(call: Call<NewChatResponse>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

    fun getUsers(token: String?, onResult: (List<ProfileResponse>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.getUsers("Bearer $token").enqueue(
            object : Callback<List<ProfileResponse>> {
                override fun onResponse(
                    call: Call<List<ProfileResponse>>,
                    response: Response<List<ProfileResponse>>
                ) {
                    val usersResponse = response.body()
                    onResult(usersResponse)
                }

                override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

}