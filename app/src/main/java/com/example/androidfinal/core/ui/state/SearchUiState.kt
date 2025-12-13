package com.example.androidfinal.core.ui.state

import com.example.androidfinal.core.data.model.NewsArticle

sealed class SearchUiState {
    data object Initial : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val articles: List<NewsArticle>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}