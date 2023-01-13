package com.example.putinder.content_screen.chats_screen.api

import com.example.putinder.content_screen.chats_screen.Chat
import com.example.putinder.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun getChatsList(token: String?, onResult: (List<Chat>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

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