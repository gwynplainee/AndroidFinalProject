package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidfinal.core.data.repositories.NewsRepository

class HomeViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(HomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}