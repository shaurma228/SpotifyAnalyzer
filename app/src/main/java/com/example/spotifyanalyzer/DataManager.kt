package com.example.spotifyanalyzer

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.spotifyanalyzer.viewmodel.History

class DataManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("SpotifyData", Context.MODE_PRIVATE)
    private val gson = Gson();

    fun saveHistoryData(historyData: Map<String, History>) {
        val json = gson.toJson(historyData)
        prefs.edit().putString("history_data", json).apply()
    }

    fun loadHistoryData(): Map<String, History> {
        val json = prefs.getString("history_data", "") ?: ""
        return if (json.isNotEmpty()) {
            gson.fromJson(json, object : TypeToken<Map<String, History>>() {}.type)
        } else {
            emptyMap()
        }
    }

    fun clearHistoryData() {
        prefs.edit().remove("history_data").apply()
    }
}

