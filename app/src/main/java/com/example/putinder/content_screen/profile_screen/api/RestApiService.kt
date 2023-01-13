package com.example.putinder.content_screen.profile_screen.api

import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.example.putinder.retrofit.ServiceBuilder
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

    fun updateUserInfo(token: String?, name: String?, image: String?, onResult: (ProfileResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.updateUserInfo("Bearer $token", name, image).enqueue(
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

}