package com.example.putinder.content_screen.map_screen

import com.google.gson.annotations.SerializedName

data class Coords(
    val lon: String,
    val lat: String
)

data class AddressResponse(
    val country: String,
    @SerializedName("locality") val city: String,
    val street: String,
    val house: String
)
