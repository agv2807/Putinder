package com.example.putinder.content_screen.profile_screen.api

import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("users/me")
    fun getUserInfoResponse(
        @Header("Authorization") token: String?
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PUT("users")
    fun updateUserPhoto(@Header("Authorization") token: String?,
                        @Field("image") image: String?
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PUT("users")
    fun updateUserName(@Header("Authorization") token: String?,
                       @Field("name") name: String?
    ): Call<ProfileResponse>
}