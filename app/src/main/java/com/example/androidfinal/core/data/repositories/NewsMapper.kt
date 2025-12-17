package com.example.androidfinal.core.data.repositories

import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.db.SavedArticleEntity
import com.example.androidfinal.core.data.model.NewsArticle

object NewsMapper {
    fun NewsArticle.toEntity(): NewsArticleEntity {
        return NewsArticleEntity(
            id = 0,
            source = this.source,
            author = this.author,
            title = this.title,
            description = this.description,
            url = this.url,
            imageUrl = this.imageUrl,
            publishedAt = this.publishedAt,
            content = this.content
        )
    }

    fun NewsArticleEntity.toNewsArticle(): NewsArticle {
        return NewsArticle(
            source = this.source,
            author = this.author,
            title = this.title,
            description = this.description,
            url = this.url,
            imageUrl = this.imageUrl,
            publishedAt = this.publishedAt,
            content = this.content
        )
    }

    fun NewsArticleEntity.toSavedEntity(): SavedArticleEntity {
        return SavedArticleEntity(
            url = this.url,
            title = this.title,
            author = this.author,
            description = this.description,
            content = this.content,
            imageUrl = this.imageUrl,
            publishedAt = this.publishedAt,
            source = this.source
        )
    }

    fun SavedArticleEntity.toNewsEntity(): NewsArticleEntity {
        return NewsArticleEntity(
            id = 0,
            url = this.url,
            title = this.title,
            author = this.author,
            description = this.description,
            content = this.content,
            imageUrl = this.imageUrl,
            publishedAt = this.publishedAt,
            source = this.source
        )
    }
}