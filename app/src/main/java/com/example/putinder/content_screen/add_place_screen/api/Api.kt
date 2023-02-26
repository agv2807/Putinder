package com.example.putinder.content_screen.add_place_screen.api

import com.example.model.place.PlaceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("places")
    fun pushPostPlace(
        @Body placeInfo: com.example.model.place.PlaceResponse
    ): Call<com.example.model.place.PlaceResponse>

}