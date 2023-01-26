package com.example.putinder.sign_screen.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.sign_screen.api.RestApiService
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.models.UserResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.UUID

class SignViewModel : ViewModel() {

    private val storageRef = Firebase.storage.reference

    val userInfoLiveData = MutableLiveData<UserResponse>()
    val userPhotoLiveData = MutableLiveData<Uri>()
    val photoIdLiveData = MutableLiveData<String>("")

    private val apiService = RestApiService()

    fun updateUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            apiService.addUser(userInfo) {
                if (it?.user?.id != null) {
                    userInfoLiveData.value = it
                } else {
                    userInfoLiveData.value = null
                }
            }
        }
    }

    fun updateAuthUserInfo(userInfo: UserInfoAuth) {
        viewModelScope.launch {
            apiService.authUser(userInfo) {
                if (it?.user?.id != null) {
                    userInfoLiveData.value = it
                } else {
                    userInfoLiveData.value = null
                }
            }
        }
    }

    fun uploadPhoto(uri: Uri) {

        viewModelScope.launch {
            val id = UUID.randomUUID()
            val reference = storageRef.child("images/${id}")
            val uploadTask = reference.putFile(uri)

            uploadTask.continueWithTask { task ->
                if (task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                reference.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    userPhotoLiveData.value = uri
                    photoIdLiveData.value = id.toString()
                }
            }
        }
    }

    fun checkToken(token: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            apiService.getUserInfoResponse(token) {
                if (it != null) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
        }
    }
}