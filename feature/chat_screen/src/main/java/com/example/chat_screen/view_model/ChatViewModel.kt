package com.example.chat_screen.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.chat.ChatRepository
import com.example.model.chat.MessageResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatViewModel : ViewModel() {

    private val apiService = ChatRepository()
    private val storageRef = Firebase.storage.reference

    val messagesLiveData = MutableLiveData<List<MessageResponse>>()
    val userImageLiveData = MutableLiveData<Uri>()

    fun initWebSocket(token: String) {
        viewModelScope.launch {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .header("Authorization", "Bearer $token")
                .url("wss://routinder-production.up.railway.app/chats/socket")
                .build()

            val listener = LocalWebSocketListener(this@ChatViewModel)
            val ws: WebSocket = client.newWebSocket(request, listener)
        }
    }

    fun loadMessages(token: String?, chatId: String) {
        viewModelScope.launch {
            apiService.getMessagesList(token, chatId) {
                if (it != null) {
                    messagesLiveData.value = it
                } else {
                    Log.d("ChatViewModel", "Чет не то с сообщениями")
                }
            }
        }
    }

    fun loadUserImage(image: String) {
        viewModelScope.launch {
            storageRef.child("images/${image}").downloadUrl.addOnSuccessListener { uri ->
                userImageLiveData.value = uri
            }.addOnFailureListener {
                Log.d("TAG", "Error load photo")
            }
        }
    }

    fun pushMessage(token: String?, message: String, chatId: String) {
        viewModelScope.launch {
            apiService.pushMessage(token, message, chatId) {
                if (it?.message != null) {
                    Log.d("ChatsViewModel", "Что-то есть в этом")
                } else {
                    Log.e("ChatsViewModel", "Error load message")
                }
            }
        }
    }

}