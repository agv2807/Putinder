package com.example.data.chats

import com.example.network.chats_api.Api
import com.example.model.chats.Chat
import com.example.network.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsRepository {

    private val retrofit = ServiceBuilder.buildService(Api::class.java)

    fun getChatsList(token: String?, onResult: (List<Chat>?) -> Unit) {

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