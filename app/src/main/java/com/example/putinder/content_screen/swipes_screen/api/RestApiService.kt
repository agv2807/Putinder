package com.example.putinder.content_screen.swipes_screen.api

import android.util.Log
import com.example.putinder.content_screen.swipes_screen.models.PlaceInfo
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.example.putinder.retrofit.ServiceBuilder
import com.example.putinder.sign_screen.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun getPlaces(onResult: (List<PlaceResponse>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(Api::class.java)

        retrofit.getPlacesResponse().enqueue(
            object : Callback<List<PlaceResponse>> {
                override fun onResponse(
                    call: Call<List<PlaceResponse>>,
                    response: Response<List<PlaceResponse>>
                ) {
                    val places = response.body()
                    onResult(places)
                    Log.d("TAG", "Запрос улетел как будто")
                }

                override fun onFailure(call: Call<List<PlaceResponse>>, t: Throwable) {
                    onResult(null)
                }

            }
        )
    }

}