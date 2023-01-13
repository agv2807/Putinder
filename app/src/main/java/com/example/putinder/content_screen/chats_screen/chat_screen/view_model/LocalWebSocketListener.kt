package com.example.putinder.content_screen.chats_screen.chat_screen.view_model

import android.util.Log
import com.example.putinder.content_screen.chats_screen.chat_screen.models.MessageResponse
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class LocalWebSocketListener(private val chatViewModel: ChatViewModel) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("Hello")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        outPut("Received $text")
        val jsonMessageResponse = JSONObject(text)
        val messageId = jsonMessageResponse.getString("id")
        val chatId = jsonMessageResponse.getString("chatId")
        val messageText = jsonMessageResponse.getString("message")
        val messageDate = jsonMessageResponse.getString("date")
        val userInfo = jsonMessageResponse.getJSONObject("user")
        val userName = userInfo.getString("name")
        val userLogin = userInfo.getString("login")
        val userImage = userInfo.getString("image")
        val userId = userInfo.getString("id")
        val user = ProfileResponse(userLogin, userId, userImage,userName)


        val messages = chatViewModel.messagesLiveData.value?.toMutableList()
        messages?.add(MessageResponse(messageText, messageDate, user))
        chatViewModel.messagesLiveData.postValue(messages)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        outPut("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        outPut("Error: ${t.message}")
    }

    private fun outPut(text: String) {
        Log.d("WebSocket", text)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

}