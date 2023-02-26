package com.example.network.auth_api

import com.example.model.user.ProfileResponse
import com.example.model.user.UserInfo
import com.example.model.user.UserInfoAuth
import com.example.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

    @POST("users")
    fun pushPost(
        @Body userInfo: UserInfo
    ): Call<UserResponse>

    @POST("users/auth")
    fun pushPostAuth(
        @Body userInfo: UserInfoAuth
    ): Call<UserResponse>

    @GET("users/me")
    fun getUserInfoResponse(
        @Header("Authorization") token: String?
    ): Call<ProfileResponse>

}