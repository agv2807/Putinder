package com.example.data.query_preferences

import android.content.Context
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_TOKEN = "searchToken"
private const val PREF_SEARCH_ID = "searchId"

object StoreUserTokenAndId {

    fun getStoredToken(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_TOKEN, "") ?: ""
    }

    fun getStoredId(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_ID, "") ?: ""
    }

    fun setStoredTokenAndId(context: Context, token: String, id: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_TOKEN, token)
            .putString(PREF_SEARCH_ID, id)
            .apply()
    }
}