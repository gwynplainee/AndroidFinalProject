package com.example.androidfinal.core.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_COUNTRY = "selected_country"
        private const val DEFAULT_COUNTRY = "us"
    }

    fun saveCountry(countryCode: String) {
        prefs.edit().putString(KEY_COUNTRY, countryCode).apply()
    }

    fun getCountry(): String {
        return prefs.getString(KEY_COUNTRY, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
    }
}