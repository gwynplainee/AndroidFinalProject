package com.example.androidfinal.core.data.repositories

import com.example.androidfinal.core.data.api.ApiService
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.NewsDao
import kotlinx.coroutines.flow.Flow


class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao)
{
    fun getNewsStream(): Flow<List<NewsArticleEntity>> {
        return newsDao.getAllArticles()
    }

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