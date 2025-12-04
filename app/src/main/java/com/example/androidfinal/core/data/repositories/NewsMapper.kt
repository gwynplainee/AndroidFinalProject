package com.example.androidfinal.core.data.repositories

import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.model.NewsArticle

object NewsMapper {
    fun toEntity(article: NewsArticle): NewsArticleEntity
        = NewsArticleEntity(
            id = 0,
            source = article.source,
            author = article.author,
            title = article.title,
            description = article.description,
            url = article.url,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt,
            content = article.content
        )

    fun toDomain(entity: NewsArticleEntity): NewsArticle
        = NewsArticle(
            source = entity.source,
            author = entity.author,
            title = entity.title,
            description = entity.description,
            url = entity.url,
            imageUrl = entity.imageUrl,
            publishedAt = entity.publishedAt,
            content = entity.content
        )


    fun toEntityList(articles: List<NewsArticle>): List<NewsArticleEntity>
        = articles.map { toEntity(it) }

    fun toDomainList(entities: List<NewsArticleEntity>): List<NewsArticle>
        = entities.map { toDomain(it) }
}