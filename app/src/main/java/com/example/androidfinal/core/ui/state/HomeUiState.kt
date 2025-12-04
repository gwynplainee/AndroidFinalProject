package com.example.androidfinal.core.ui.state

import com.example.androidfinal.core.data.db.NewsArticleEntity

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val articles: List<NewsArticleEntity>): HomeUiState()
    data class Error(val message: String) : HomeUiState()

}