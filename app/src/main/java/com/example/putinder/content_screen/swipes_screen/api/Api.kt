package com.example.putinder.content_screen.swipes_screen.api

import com.example.putinder.content_screen.swipes_screen.models.PlaceInfo
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("places")
    fun getPlacesResponse(): Call<List<PlaceResponse>>

}