package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.utils.PreferencesManager

class HomeViewModelFactory(private val newsRepository: NewsRepository, private val preferencesManager: PreferencesManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(HomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(newsRepository, preferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}