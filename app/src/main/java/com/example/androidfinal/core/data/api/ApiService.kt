package com.example.androidfinal.core.data.api

import com.example.androidfinal.core.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/everything")
    suspend fun getNewsArticles(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ) : NewsResponse
}