package com.example.androidfinal.core.data.repositories

import android.util.Log
import com.example.androidfinal.core.data.api.ApiService
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.NewsDao
import kotlinx.coroutines.flow.Flow
import com.example.androidfinal.core.data.api.ApiClient
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.data.db.SavedArticlesDao

class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val savedArticlesDao: SavedArticlesDao)
{
    val newsStream: Flow<List<NewsArticleEntity>> = newsDao.getAllArticles()
    val savedNewsStream: Flow<List<SavedArticleEntity>> = savedArticlesDao.getAllSavedArticles()

    suspend fun fetchAndCacheNews(country: String, category: String, apiKey: String) {
        try{
            val response = ApiClient.apiService.getNewsArticles(country, category, apiKey)

            if(response.status == "ok"){
                val entities = response.articles.map { article -> NewsArticleEntity(
                    author = article.author,
                    source = article.source,
                    title = article.title ?: "No Title",
                    url = article.url,
                    imageUrl = article.imageUrl,
                    description = article.description,
                    content = article.content,
                    publishedAt = article.publishedAt

                ) }
                newsDao.deleteAllArticles()
                newsDao.insertAll(entities)
            }
        } catch (e: Exception){
            Log.e("NewsRepository", "Network refresh failed: ${e.message}")
        }
    }

    suspend fun saveArticle(articleEntity: NewsArticleEntity){
        val savedEntity = SavedArticleEntity(
            url = articleEntity.url,
            title = articleEntity.title,
            author = articleEntity.author,
            description = articleEntity.description,
            content = articleEntity.content,
            imageUrl = articleEntity.imageUrl,
            publishedAt = articleEntity.publishedAt
        )
        savedArticlesDao.insertArticle(savedEntity)
    }

    suspend fun deleteArticle(articleEntity: SavedArticleEntity){
        savedArticlesDao.deleteArticle(articleEntity)
    }



}