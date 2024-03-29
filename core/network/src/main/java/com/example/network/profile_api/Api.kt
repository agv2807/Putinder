package com.example.network.profile_api

import com.example.model.user.ProfileResponse
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