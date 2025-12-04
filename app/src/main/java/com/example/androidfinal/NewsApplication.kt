package com.example.androidfinal

import android.app.Application
import com.example.androidfinal.core.data.api.ApiClient
import com.example.androidfinal.core.data.db.NewsDatabase
import com.example.androidfinal.core.data.repositories.NewsRepository

class NewsApplication: Application() {
    val database by lazy { NewsDatabase.getDatabase(this) }

    val repository by lazy {
        NewsRepository(
            apiService = ApiClient.apiService,
            newsDao = database.newsDao()
        )
    }
}