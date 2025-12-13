package com.example.androidfinal.core.ui.adapter

import com.example.androidfinal.core.data.db.SavedArticleEntity

interface OnSavedArticleClickListener {
    fun onArticleClicked(article: SavedArticleEntity)
}