package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidfinal.core.data.db.NewsArticleEntity

class DetailViewModelFactory(private val article: NewsArticleEntity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(article) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}