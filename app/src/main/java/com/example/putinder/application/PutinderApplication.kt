package com.example.putinder.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mapkit.MapKitFactory

class PutinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MapKitFactory.setApiKey("7f8c389e-9b18-4bec-a68e-0585dcd64bbf")
    }
}