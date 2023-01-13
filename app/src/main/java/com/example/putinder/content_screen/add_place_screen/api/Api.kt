package com.example.putinder.content_screen.add_place_screen.api

import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("places")
    fun pushPostPlace(
        @Body placeInfo: PlaceResponse
    ): Call<PlaceResponse>

}