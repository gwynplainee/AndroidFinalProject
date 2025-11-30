package com.example.androidfinal.core.data.db

import androidx.room.Database

@Database(entities = [NewsArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase {
    abstract fun newsDao() : NewsDao
}