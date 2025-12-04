package com.example.androidfinal.core.ui.state

import com.example.androidfinal.core.data.db.NewsArticleEntity

data class DetailUiState(
    val article: NewsArticleEntity,
    val formattedMetadata: String = "",
    val formattedBody: String = ""
)
