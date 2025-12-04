package com.example.androidfinal.core.data.repositories

import com.example.androidfinal.core.data.api.ApiService
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.NewsDao
import com.example.androidfinal.core.data.model.NewsArticle
import com.example.androidfinal.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao)
{
    suspend fun getTopHeadlines(category: String? = null): List<NewsArticle> {
        val response = apiService.getNewsArticles(country = Constants.DEFAULT_COUNTRY, category = category, apiKey = Constants.NEWS_API_KEY)

        if (response.isSuccessful && response.body() != null) {
            val newsResponse = response.body()!!
            if (newsResponse.status == "ok") return newsResponse.articles
            else throw Exception("API returned error status!")
        }
        else throw Exception("Failed to fetch news: ${response.message()}")
    }

    fun getBookmarkedArticles(): Flow<List<NewsArticle>>
        = newsDao.getAllArticles().map { entities -> NewsMapper.toDomainList(entities) }

    suspend fun bookmarkArticle(article: NewsArticle) : Unit
        = newsDao.insertArticle(NewsMapper.toEntity(article))

    suspend fun removeBookmark(article: NewsArticle) {
        val entity = NewsMapper.toEntity(article)
        newsDao.deleteArticle(entity)
    }

    fun isArticleBookmarked(articleId: Int): Flow<Boolean> =
        newsDao.getArticleById(articleId).map { it != null }

    suspend fun clearAllBookmarks(): Unit = newsDao.deleteAllArticles()

//    fun getNewsStream(): Flow<List<NewsArticleEntity>> {
//        return newsDao.getAllArticles()
//    }

//    suspend fun fetchAndCacheNews() {
//        try{
//            val response = apiService.getNewsArticles("world news", NEWS_API_KEY)
//
//            if(response.status == "ok"){
//                val entities = response.articles.map { article -> NewsArticleEntity(
//
//                ) }
//            }
//        }
//    }



}