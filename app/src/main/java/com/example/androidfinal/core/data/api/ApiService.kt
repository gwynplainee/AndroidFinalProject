package com.example.androidfinal.core.data.api

import com.example.androidfinal.core.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getNewsArticles(
        @Query("sources") source: String?,
        @Query("category") category: String?,
//        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ) : NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 50,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String
    ) : NewsResponse
}