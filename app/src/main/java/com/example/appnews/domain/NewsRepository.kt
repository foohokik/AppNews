package com.example.appnews.domain

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.domain.model.Article
import com.example.appnews.domain.model.News
import com.example.appnews.domain.model.Sources
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getHeadlinesNews(
        country: String,
        category: String,
        pageNumber: Int): NetworkResult<News>
    suspend fun getSearchNews(
        category: String,
        searchQuery: String,
        pageNumber: Int
    ): NetworkResult<News>
    suspend fun getSources(): NetworkResult<Sources>
    suspend fun getSourcesNews(sourceId: String): NetworkResult<News>
    suspend fun getSearchNewsFromSource(sourceId: String, searchQuery: String): NetworkResult<News>
    suspend fun upsert(article: Article)
    suspend fun delete(title: String)
    suspend fun getAllSavedArticles(): List<Article>
    suspend fun deleteAll()
    suspend fun getArticle(title: String): Boolean
    suspend fun getSearchSavedArticle(title: String):List<Article>
}