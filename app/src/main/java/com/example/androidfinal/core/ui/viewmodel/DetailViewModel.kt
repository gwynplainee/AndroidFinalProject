package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.R
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.ui.state.DetailUiState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: NewsRepository,article: NewsArticleEntity) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState(article))
    val uiState: StateFlow<DetailUiState> = _uiState
    private val _isArticleSaved = MutableStateFlow(false)
    val isArticleSaved: StateFlow<Boolean> = _isArticleSaved.asStateFlow()
    private val _event = MutableSharedFlow<String>()
    val event: SharedFlow<String> = _event.asSharedFlow()

    init {
        processArticleContent(article)
        checkIfArticleSaved()
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

    private fun checkIfArticleSaved(){
        viewModelScope.launch {
            _isArticleSaved.value = repository.isArticleSaved(_uiState.value.article.url)
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            if (_isArticleSaved.value) {
                repository.deleteArticleByUrl(_uiState.value.article.url)
                _isArticleSaved.value = false
                _event.emit("Article removed from bookmarks.")
            } else {
                repository.saveArticle(_uiState.value.article)
                _isArticleSaved.value = true
                _event.emit("Article added to bookmarks.")
            }
        }
    }




}