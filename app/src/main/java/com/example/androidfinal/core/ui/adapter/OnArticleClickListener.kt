package com.example.androidfinal.core.ui.adapter

import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.SavedArticleEntity

interface OnArticleClickListener {
    fun onArticleClicked(article: NewsArticleEntity)
}