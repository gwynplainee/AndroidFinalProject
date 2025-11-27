package com.example.androidfinal.core.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResult: Int,

    @SerializedName("articles")
    val articles: List<NewsArticle>
)
