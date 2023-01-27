package com.example.putinder.content_screen.swipes_screen.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.putinder.content_screen.swipes_screen.api.RestApiService
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class SwipesViewModel : ViewModel() {

    val places = MutableLiveData<List<PlaceResponse>>()
    var place: PlaceResponse? = null

    private val apiService = RestApiService()
    private val storageRef = Firebase.storage.reference

    init {
        updatePlaces()
    }

    private fun updatePlaces() {
        viewModelScope.launch {
            apiService.getPlaces {
                if (!it.isNullOrEmpty()) {
                    val listPlaceResponse = it
                    listPlaceResponse.forEach { response ->
                        response.uri = mutableListOf()
                        response.images.forEach { id ->
                            storageRef.child("images/${id}").downloadUrl.addOnSuccessListener { uri ->
                                response.uri.add(uri)
                                places.value = listPlaceResponse!!
                            }.addOnFailureListener {
                                Log.d("TAG", "Error load photo")
                            }
                        }
                    }
                } else {
                    Log.d("TAG", "Error update places")
                }
            }
        }
    }
}