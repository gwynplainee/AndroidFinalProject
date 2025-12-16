package com.example.androidfinal.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: SavedArticleEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE url = :url)")
    suspend fun isArticleSaved(url: String?): Boolean

    @Query("SELECT * FROM saved_articles ORDER BY id DESC")
    fun getAllSavedArticles(): Flow<List<SavedArticleEntity>>

    @Query("DELETE FROM saved_articles WHERE url = :url")
    suspend fun deleteArticleByUrl(url: String?)

    @Delete
    suspend fun deleteArticle(article: SavedArticleEntity)

}