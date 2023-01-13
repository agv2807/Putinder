package com.example.putinder.content_screen.chats_screen.create_chat_screen.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.content_screen.chats_screen.create_chat_screen.models.NewChatResponse
import com.example.putinder.content_screen.chats_screen.create_chat_screen.api.RestApiService
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class CreateChatViewModel : ViewModel() {

    private val apiService = RestApiService()
    private val storageRef = Firebase.storage.reference

    val usersListLiveData = MutableLiveData<List<ProfileResponse>>()
    val newChatLiveData = MutableLiveData<NewChatResponse>()

    fun getUsersList(token: String?) {
        viewModelScope.launch {
            apiService.getUsers(token) {
                if (it != null) {
                    usersListLiveData.value = it
                } else {
                    Log.e("CreateChatViewModel", "Error load users")
                }
            }
        }
    }

    fun createNewChat(token: String?, userId: String, onResult: (NewChatResponse?) -> Unit) {
        viewModelScope.launch {
            apiService.createNewChat(token, userId) {
                if (it?.id != null) {
                    newChatLiveData.value = it
                    onResult(it)
                } else {
                    Log.e("CreateChatViewModel", "Error create new chat")
                    onResult(null)
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