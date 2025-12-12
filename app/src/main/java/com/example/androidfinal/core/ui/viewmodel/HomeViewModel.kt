package com.example.androidfinal.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.androidfinal.BuildConfig
//import com.example.androidfinal.BuildConfig.NEWS_API_KEY
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.repositories.NewsRepository
import com.example.androidfinal.core.ui.state.HomeUiState
import com.example.androidfinal.core.utils.Constants.NEWS_API_KEY
import com.example.androidfinal.core.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeViewModel(private val repository: NewsRepository, private val preferencesManager: PreferencesManager) : ViewModel() {
    private val _currentCategory = MutableStateFlow("general")
    val uiState: StateFlow<HomeUiState> = _currentCategory.flatMapLatest { category -> repository.newsStream.map { articleEntities ->
        if(articleEntities.isEmpty()){
            HomeUiState.Loading
        }else{
            HomeUiState.Success(articleEntities)
        }
    }.catch { e -> emit(HomeUiState.Error("Database error: ${e.message}")) }
        .onStart { refreshNewsFromNetwork(category) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState.Loading)

    private val _scrollToTopEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTopEvent: SharedFlow<Unit> = _scrollToTopEvent.asSharedFlow()



    fun refreshNews(category: String){
        if(_currentCategory.value != category){
            _currentCategory.value = category
        }else{
            refreshNewsFromNetwork(category)
        }

    }

    fun refreshNewsFromNetwork(category: String){
        val country = preferencesManager.getCountry()
        viewModelScope.launch (Dispatchers.IO){
            repository.fetchAndCacheNews(country,category,NEWS_API_KEY)
        }
    }

    fun refreshNewsForCountry() {
        refreshNews(_currentCategory.value)
    }

    fun requestScrollToTop(){
        viewModelScope.launch {
            _scrollToTopEvent.emit(Unit)
        }

    }
}