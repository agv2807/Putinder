package com.example.putinder.content_screen.add_place_screen.api

import android.util.Log
import com.example.model.place.PlaceResponse
import com.example.network.retrofit.ServiceBuilder
import com.example.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun addPlace(placeInfo: com.example.model.place.PlaceResponse, onResult: (com.example.model.UserResponse?) -> Unit) {
        val retrofit = com.example.network.retrofit.ServiceBuilder.buildService(Api::class.java)

        retrofit.pushPostPlace(placeInfo).enqueue(
            object : Callback<com.example.model.place.PlaceResponse> {
                override fun onResponse(
                    call: Call<com.example.model.place.PlaceResponse>,
                    response: Response<com.example.model.place.PlaceResponse>
                ) {
                    Log.d("TAG", "Ура ура что-то пришло")
                }

                override fun onFailure(call: Call<com.example.model.place.PlaceResponse>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

}