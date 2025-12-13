package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.ui.state.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: NewsRepository,article: NewsArticleEntity) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState(article))
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        processArticleContent(article)
    }

    private fun processArticleContent(article: NewsArticleEntity) {
        viewModelScope.launch {
            var bodyText = article.content ?: article.description ?: "No content available."
            val truncationMarker = bodyText.lastIndexOf(" [+")
            if (truncationMarker != -1) {
                bodyText = bodyText.substring(0, truncationMarker) + "..."
            }

            _uiState.value = DetailUiState(
                article = article,
                formattedMetadata = "${article.author ?: "Source"} | ${article.publishedAt}",
                formattedBody = bodyText
            )
        }
    }

    fun saveCurrentArticle(){
        viewModelScope.launch {
            repository.saveArticle(_uiState.value.article)

        }
    }
}