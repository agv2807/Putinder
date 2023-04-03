package com.example.data.swipes

import android.util.Log
import com.example.model.place.PlaceResponse
import com.example.network.place_api.Api
import com.example.network.retrofit.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipesRepository {

    private val retrofit = ServiceBuilder.buildService(Api::class.java)

    fun getPlaces(onResult: (List<PlaceResponse>?) -> Unit) {

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