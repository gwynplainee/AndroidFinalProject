package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.ui.state.SearchUiState
import com.example.androidfinal.core.utils.Constants.NEWS_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun searchNews(query: String) {
        searchJob?.cancel()

        if (query.isEmpty()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        // wait before search
        searchJob = viewModelScope.launch(Dispatchers.IO){
            delay(500L)
            _uiState.value = SearchUiState.Loading

            try {
                val articles = repository.searchNews(query, NEWS_API_KEY)
                if (!articles.isEmpty()) {
                    _uiState.value = SearchUiState.Success(articles)
                } else {
                    _uiState.value = SearchUiState.Error("No results found for: $query")
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Search failed: ${e.message}")
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = SearchUiState.Initial
    }
}