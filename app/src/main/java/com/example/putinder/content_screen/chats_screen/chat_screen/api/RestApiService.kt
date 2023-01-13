package com.example.putinder.content_screen.chats_screen.chat_screen.api

import com.example.putinder.content_screen.chats_screen.chat_screen.models.MessageResponse
import com.example.putinder.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun pushMessage(token: String?, message: String, chatId: String, onResult: (MessageResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.pushMessage("Bearer $token", chatId, message).enqueue(
            object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    val messageResponse = response.body()
                    onResult(messageResponse)
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

    fun getMessagesList(token: String?, chatId: String, onResult: (List<MessageResponse>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.getMessagesList("Bearer $token", chatId).enqueue(
            object : Callback<List<MessageResponse>> {
                override fun onResponse(
                    call: Call<List<MessageResponse>>,
                    response: Response<List<MessageResponse>>
                ) {
                    val messagesResponse = response.body()
                    onResult(messagesResponse)
                }

                override fun onFailure(call: Call<List<MessageResponse>>, t: Throwable) {
                    onResult(null)
                }
            }

        )
    }

}