package com.example.androidfinal.core.data.db

import androidx.room.Database
import com.example.androidfinal.core.utils.Constants

@Database(entities = [NewsArticleEntity::class], version = Constants.DATABASE_VERSION, exportSchema = false)
abstract class NewsDatabase {
    abstract fun newsDao() : NewsDao
}