package com.example.androidfinal.core.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/everything")
    suspend fun getNewsArticles(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ) : NewsResponse
}