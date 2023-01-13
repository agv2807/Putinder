package com.example.putinder.sign_screen.api

import android.util.Log
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.example.putinder.retrofit.ServiceBuilder
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun getUserInfoResponse(token: String?, onResult: (ProfileResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.getUserInfoResponse("Bearer $token").enqueue(
            object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    val userInfo = response.body()
                    onResult(userInfo)
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

    fun addUser(userInfo: UserInfo, onResult: (UserResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.pushPost(userInfo).enqueue(
            object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }

    fun authUser(userInfo: UserInfoAuth, onResult: (UserResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.pushPostAuth(userInfo).enqueue(
            object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val authUser = response.body()
                    onResult(authUser)
                    Log.d("TAG", "Запрос покинул чат")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    onResult(null)
                }
            }
        )
    }

}