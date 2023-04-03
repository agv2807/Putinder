package com.example.putinder.content_screen.map_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    val address = MutableLiveData<AddressResponse>()

    private val apiService = RestApiService()



}