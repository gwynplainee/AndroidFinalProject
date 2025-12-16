package com.example.androidfinal.core.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SOURCE = "selected_source"

        private const val KEY_CATEGORY = "selected_category"
        private const val DEFAULT_CATEGORY = "general"
    }

    fun saveSource(sourceCode: String?) {
        prefs.edit().putString(KEY_SOURCE, sourceCode).apply()
    }

    fun getSource(): String? {
        return prefs.getString(KEY_SOURCE, null)
    }

    fun savecategory(category: String){
        prefs.edit().putString(KEY_CATEGORY, category.lowercase()).apply()
    }

    fun getCategory(): String{
        return prefs.getString(KEY_CATEGORY, DEFAULT_CATEGORY)?: DEFAULT_CATEGORY
    }

    fun setPreferencesChanged(changed: Boolean) {
        prefs.edit().putBoolean("prefs_changed", changed).apply()
    }

    fun hasPreferencesChanged(): Boolean {
        return prefs.getBoolean("prefs_changed", false)
    }
}