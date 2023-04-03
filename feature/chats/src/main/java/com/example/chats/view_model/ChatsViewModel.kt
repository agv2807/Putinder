package com.example.chats.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.chats.ChatsRepository
import com.example.model.chats.Chat
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatsViewModel : ViewModel() {

    val chatsLiveData = MutableLiveData<List<Chat>>()

    private val apiService = ChatsRepository()
    private val storageRef = Firebase.storage.reference

    fun initWebSocket(token: String) {
        viewModelScope.launch {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .header("Authorization", "Bearer $token")
                .url("wss://routinder-production.up.railway.app/chats/socket")
                .build()

            val listener = LocalWebSocketListener(this@ChatsViewModel, token)
            val ws: WebSocket = client.newWebSocket(request, listener)
        }
    }

    fun loadChatsList(token: String?) {
        viewModelScope.launch {
            apiService.getChatsList(token) {
                if (!it.isNullOrEmpty()) {
                    chatsLiveData.value = it
                } else {
                    Log.d("ChatsViewModel", "Error load chats")
                }
            }
        }
    }

    fun loadImage(image: String, onResult: (Uri?) -> Unit) {
        viewModelScope.launch {
            storageRef.child("images/${image}").downloadUrl.addOnSuccessListener {
                onResult(it)
            }.addOnFailureListener {
                onResult(null)
                Log.d("ChatsViewModelPhotoLoad", "Error load photo")
            }
        }
    }
}