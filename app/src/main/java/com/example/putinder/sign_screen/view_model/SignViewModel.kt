package com.example.putinder.sign_screen.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.sign_screen.api.RestApiService
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.models.UserResponse
import kotlinx.coroutines.launch

class SignViewModel : ViewModel() {

    val userInfoLiveData = MutableLiveData<UserResponse?>()
    val loading = MutableLiveData(false)

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