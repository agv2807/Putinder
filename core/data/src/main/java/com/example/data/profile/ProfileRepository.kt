package com.example.data.profile

import com.example.model.user.ProfileResponse
import com.example.network.profile_api.Api
import com.example.network.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    private val retrofit = ServiceBuilder.buildService(Api::class.java)

    fun getUserInfoResponse(token: String?, onResult: (ProfileResponse?) -> Unit) {

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

    fun updateUserPhoto(token: String?, image: String?, onResult: (ProfileResponse?) -> Unit) {

        retrofit.updateUserPhoto("Bearer $token", image).enqueue(
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

    fun updateUserName(token: String?, name: String?, onResult: (ProfileResponse?) -> Unit) {

        retrofit.updateUserName("Bearer $token", name).enqueue(
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