package com.example.create_chat.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.chat.NewChatResponse
import com.example.data.create_chat.RestApiService
import com.example.model.user.ProfileResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class CreateChatViewModel : ViewModel() {

    private val apiService = RestApiService()
    private val storageRef = Firebase.storage.reference

    val usersListLiveData = MutableLiveData<List<ProfileResponse>>()

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
                Log.d("CreateChatsLoadPhoto", "Error load photo")
            }
        }
    }
}