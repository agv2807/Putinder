package com.example.network.place_api

import com.example.model.place.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("places")
    fun getPlacesResponse(): Call<List<PlaceResponse>>

}