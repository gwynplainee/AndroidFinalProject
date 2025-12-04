package com.example.androidfinal.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticleEntity)
    @Query("SELECT * FROM news_articles")
    fun getAllArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :id")
    fun getArticleById(id: Int): Flow<NewsArticleEntity?>

    @Delete
    suspend fun deleteArticle(article: NewsArticleEntity)

    @Query("DELETE FROM news_articles")
    suspend fun deleteAllArticles()
}