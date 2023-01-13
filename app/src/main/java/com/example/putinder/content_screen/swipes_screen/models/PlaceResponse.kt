package com.example.putinder.content_screen.swipes_screen.models

import android.net.Uri

data class PlaceResponse(
    val lon: String,
    val title: String,
    val images: List<String>,
    val lat: String,
    val description: String,
    val id: String,
    var uri: MutableList<Uri> = mutableListOf()
)
