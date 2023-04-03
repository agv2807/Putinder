package com.example.data.query_preferences

import android.content.Context
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_LAT = "lan"
private const val PREF_SEARCH_LON = "lon"

object StoreCoordinates {
    fun getStoredLat(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_LAT, "") ?: ""
    }

    fun getStoredLon(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_LON, "") ?: ""
    }

    fun setStoredCoordinates(context: Context, lat: String, lon: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_LAT, lat)
            .putString(PREF_SEARCH_LON, lon)
            .apply()
    }
}