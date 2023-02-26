package com.example.data.create_chat

import com.example.model.chat.NewChatResponse
import com.example.model.user.ProfileResponse
import com.example.network.create_chat_api.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun createNewChat(token: String?, userId: String, onResult: (NewChatResponse?) -> Unit) {
        val retrofit = com.example.network.retrofit.ServiceBuilder.buildService(Api::class.java)

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
        val retrofit = com.example.network.retrofit.ServiceBuilder.buildService(Api::class.java)

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