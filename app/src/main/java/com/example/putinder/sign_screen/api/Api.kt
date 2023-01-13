package com.example.putinder.sign_screen.api

import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.models.UserResponse
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