package com.example.spotifyanalyzer

import android.content.Context
import android.content.SharedPreferences

class DataManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("SpotifyData", Context.MODE_PRIVATE)

    fun saveJsonData(json: String) {
        prefs.edit().putString("json_data", json).apply()
    }

    fun loadJsonData(): String {
        return prefs.getString("json_data", "") ?: ""
    }

    fun clearJsonData() {
        prefs.edit().remove("json_data").apply()
    }
}
