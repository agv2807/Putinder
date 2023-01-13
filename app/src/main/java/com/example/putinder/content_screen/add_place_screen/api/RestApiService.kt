package com.example.putinder.content_screen.add_place_screen.api

import android.util.Log
import com.example.putinder.content_screen.swipes_screen.models.PlaceInfo
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.example.putinder.retrofit.ServiceBuilder
import com.example.putinder.sign_screen.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun addPlace(placeInfo: PlaceResponse, onResult: (UserResponse?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.pushPostPlace(placeInfo).enqueue(
            object : Callback<PlaceResponse> {
                override fun onResponse(
                    call: Call<PlaceResponse>,
                    response: Response<PlaceResponse>
                ) {
                    Log.d("TAG", "Ура ура что-то пришло")
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

}