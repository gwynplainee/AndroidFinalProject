package com.example.androidfinal.core.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidfinal.core.data.model.Source
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val content: String?
): Parcelable
