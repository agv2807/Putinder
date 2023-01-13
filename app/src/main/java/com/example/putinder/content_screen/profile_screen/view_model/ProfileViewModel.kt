package com.example.putinder.content_screen.profile_screen.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.content_screen.profile_screen.api.RestApiService
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel : ViewModel() {

    val userInfoLiveData = MutableLiveData<ProfileResponse>()
    val userPhotoLiveData = MutableLiveData<Uri>()

    private val apiService = RestApiService()

    private val storageRef = Firebase.storage.reference

    private var myToken: String? = null

    fun loadUserInfo(token: String?) {
        myToken = token
        viewModelScope.launch {
            apiService.getUserInfoResponse(token) {
                if (it?.id != null) {
                    userInfoLiveData.value = it
                    storageRef.child("images/${it.image}").downloadUrl.addOnSuccessListener {
                        userPhotoLiveData.value = it
                    }.addOnFailureListener {
                        Log.d("TAG", "Error load photo")
                    }
                } else {
                    Log.d("TAG", "Error load user info")
                }
            }
        }
    }

    private fun updateUserInfo(name: String?, image: String?) {
        apiService.updateUserInfo(myToken, name, image) {
            if (it?.image != null) {
                storageRef.child("images/${image}").downloadUrl.addOnSuccessListener { uri ->
                    userPhotoLiveData.value = uri
                }.addOnFailureListener {
                    Log.d("TAG", "Error load photo")
                }
            } else {
                Log.d("TAG", "Error load user info")
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
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInfo(null, id.toString())
                }
            }
        }
    }

}