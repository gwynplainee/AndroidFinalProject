package com.example.androidfinal.core.ui.adapter

import com.example.androidfinal.core.data.model.NewsArticle

interface OnArticleClickListener {
    fun onArticleClicked(article: NewsArticle)
}