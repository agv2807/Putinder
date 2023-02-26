package com.example.swipes.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.swipes.RestApiService
import com.example.model.place.PlaceResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class SwipesViewModel : ViewModel() {

    val places = MutableLiveData<List<PlaceResponse>>()
    var index = 0
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
                                places.value = listPlaceResponse
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