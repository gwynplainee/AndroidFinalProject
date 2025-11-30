package com.example.androidfinal.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticleEntity>)

    @Query("SELECT * FROM news_articles")
    fun getAllArticles(): Flow<List<NewsArticleEntity>>
}