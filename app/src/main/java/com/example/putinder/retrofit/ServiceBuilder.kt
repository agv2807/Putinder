package com.example.putinder.retrofit

import com.yandex.mapkit.MapKitFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://routinder-production.up.railway.app")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

    private val geoCodeRetrofit = Retrofit.Builder()
        .baseUrl("https://geocode-maps.yandex.ru/1.x/?apikey=7f8c389e-9b18-4bec-a68e-0585dcd64bbf")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildGeoCodeService(service: Class<T>): T{
        return geoCodeRetrofit.create(service)
    }
}