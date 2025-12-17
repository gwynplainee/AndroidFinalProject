package com.example.androidfinal.core.data.repositories

import android.util.Log
import com.example.androidfinal.core.data.api.ApiService
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.NewsDao
import kotlinx.coroutines.flow.Flow
import com.example.androidfinal.core.data.api.ApiClient
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.data.db.SavedArticlesDao
import com.example.androidfinal.core.data.model.NewsArticle
import com.example.androidfinal.core.data.repositories.NewsMapper.toEntity
import com.example.androidfinal.core.data.repositories.NewsMapper.toSavedEntity

class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val savedArticlesDao: SavedArticlesDao)
{
    val newsStream: Flow<List<NewsArticleEntity>> = newsDao.getAllArticles()
    val savedNewsStream: Flow<List<SavedArticleEntity>> = savedArticlesDao.getAllSavedArticles()

    suspend fun fetchAndCacheNews(source: String?, category: String?, apiKey: String) {
        try{
            val apiCategory = if (!source.isNullOrEmpty()) null else category
            val apiSource = source?.ifEmpty { null }

            Log.d("NewsRepository", "Fetching -> Source: $apiSource, Category: $apiCategory")
            val response = apiService.getNewsArticles(
                source = apiSource,
                category = apiCategory,
                apiKey = apiKey
            )
                //ApiClient.apiService.getNewsArticles(source, category = if (source == null) category else null, apiKey)

            if(response.status == "ok"){
                val entities = response.articles.map { it.toEntity() }
                newsDao.deleteAllArticles()
                newsDao.insertAll(entities)
            }
        } catch (e: Exception){
            Log.e("NewsRepository", "Network refresh failed: ${e.message}")
        }
    }

    suspend fun saveArticle(articleEntity: NewsArticleEntity){
        val savedEntity = articleEntity.toSavedEntity()
        savedArticlesDao.insertArticle(savedEntity)
    }

    suspend fun reSaveArticle(article: SavedArticleEntity) {
        savedArticlesDao.insertArticle(article)
    }

    suspend fun deleteArticleByUrl(url: String?){
        savedArticlesDao.deleteArticleByUrl(url)
    }

    suspend fun isArticleSaved(url: String?): Boolean {
        return savedArticlesDao.isArticleSaved(url)
    }

    suspend fun deleteArticle(article: SavedArticleEntity){
        savedArticlesDao.deleteArticle(article)
    }

    suspend fun searchNews(query: String, apiKey: String): List<NewsArticle> {
        return try {
            val resp = apiService.searchNews(query, apiKey = apiKey)

            if (resp.status == "ok") {
                resp.articles
            } else {
                Log.w("NewsRepository", "Search failed: ${resp.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Search error: ${e.message}")
            emptyList()
        }
    }



}