package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.BuildConfig
import com.example.androidfinal.BuildConfig.NEWS_API_KEY
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.ui.state.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.newsStream
                .map<List<NewsArticleEntity>, HomeUiState> { articleEntities ->
                    HomeUiState.Success(articleEntities)
                }
                .catch { e ->
                    emit(HomeUiState.Error("Database error: ${e.message}"))
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }

        refreshNews("general")
    }

    fun refreshNews(category: String){
        viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = HomeUiState.Loading


            repository.fetchAndCacheNews(category, NEWS_API_KEY)
        }
    }


}