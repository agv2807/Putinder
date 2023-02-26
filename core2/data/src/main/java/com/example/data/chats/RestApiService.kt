package com.example.data.chats

import com.example.network.chats_api.Api
import com.example.putinder.content_screen.chats_screen.models.Chat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun getChatsList(token: String?, onResult: (List<Chat>?) -> Unit) {
        val retrofit = com.example.network.retrofit.ServiceBuilder.buildService(Api::class.java)

        retrofit.getChatsResponse("Bearer $token").enqueue(
            object : Callback<List<Chat>> {
                override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                    val chatsResponse = response.body()
                    onResult(chatsResponse)
                }

                override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

}