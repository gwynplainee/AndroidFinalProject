package com.example.androidfinal.core.data.repositories

import android.util.Log
import com.example.androidfinal.core.data.api.ApiService
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.NewsDao
import kotlinx.coroutines.flow.Flow
import com.example.androidfinal.core.data.api.ApiClient

class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao)
{
    val newsStream: Flow<List<NewsArticleEntity>> = newsDao.getAllArticles()


    suspend fun fetchAndCacheNews(category: String, apiKey: String) {
        try{
            val response = ApiClient.apiService.getNewsArticles(category, apiKey)

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



}