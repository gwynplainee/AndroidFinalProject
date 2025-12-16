package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.data.repositories.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarksViewModel(private val repository: NewsRepository) : ViewModel() {

    val bookmarkedArticles: StateFlow<List<SavedArticleEntity>> =
        repository.savedNewsStream.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteArticle(article: SavedArticleEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(article)
        }
    }

    fun saveArticle(article: SavedArticleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reSaveArticle(article)
        }
    }
}