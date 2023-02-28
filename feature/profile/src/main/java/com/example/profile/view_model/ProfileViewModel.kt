package com.example.profile.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.profile.ProfileRepository
import com.example.model.user.ProfileResponse
import com.example.profile.di.DaggerProfileComponent
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    val userInfoLiveData = MutableLiveData<ProfileResponse>()
    val userPhotoLiveData = MutableLiveData<Uri>()

    @Inject lateinit var apiService: ProfileRepository

    private val storageRef = Firebase.storage.reference

    private var myToken: String? = null

    init {
        DaggerProfileComponent.create().inject(this)
    }

    fun loadUserInfo(token: String?) {
        myToken = token
        viewModelScope.launch {
            apiService.getUserInfoResponse(token) {
                if (it?.id != null) {
                    userInfoLiveData.value = it
                    storageRef.child("images/${it.image}").downloadUrl.addOnSuccessListener { uri ->
                        userPhotoLiveData.value = uri
                    }.addOnFailureListener {
                        Log.d("TAG", "Error load photo")
                    }
                } else {
                    Log.d("TAG", "Error load user info")
                }
            }
        }
    }

    private fun updateUserPhoto(image: String?) {
        viewModelScope.launch {
            apiService.updateUserPhoto(myToken, image) {
                if (it?.image != null) {
                    storageRef.child("images/${image}").downloadUrl.addOnSuccessListener { uri ->
                        userPhotoLiveData.value = uri
                    }.addOnFailureListener {
                        Log.d("TAG", "Error load photo")
                    }
                } else {
                    Log.d("TAG", "Image = null")
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
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserPhoto(id.toString())
                }
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            apiService.updateUserName(myToken, name) {
                if (it?.name != null) {
                    userInfoLiveData.value = it
                } else {
                    Log.d("ProfileViewModel", "Error load name")
                }
            }
        }
    }

}