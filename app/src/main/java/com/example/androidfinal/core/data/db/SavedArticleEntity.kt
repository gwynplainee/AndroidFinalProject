package com.example.androidfinal.core.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String?,
    val imageUrl: String?,
    val title: String,
    val author: String?,
    val description: String?,
    val content: String?,
    val publishedAt: String?
): Parcelable
