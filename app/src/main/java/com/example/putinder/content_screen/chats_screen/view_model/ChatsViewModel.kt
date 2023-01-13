package com.example.putinder.content_screen.chats_screen.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.content_screen.chats_screen.Chat
import com.example.putinder.content_screen.chats_screen.api.RestApiService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class ChatsViewModel : ViewModel() {

    val chatsLiveData = MutableLiveData<List<Chat>>()

    private val apiService = RestApiService()
    private val storageRef = Firebase.storage.reference

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