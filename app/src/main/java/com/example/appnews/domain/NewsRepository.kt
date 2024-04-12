package com.example.appnews.domain

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.toNetworkResultNews
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

    suspend fun getSources(): NetworkResult<AllSources>

    suspend fun getSourcesNews(sourceId: String): NetworkResult<News>

    suspend fun getSearchNewsFromSource(sourceId: String, searchQuery: String): NetworkResult<News>

    suspend fun upsert(article: ArticlesUI.Article)
    suspend fun delete(title: String)
    fun getAllSavedArticles(): Flow<MutableList<ArticlesUI.Article>>
    suspend fun deleteAll()
    suspend fun getArticle(title: String): Boolean
    fun getSearchSavedArticle(title: String): Flow<List<ArticlesUI.Article>>
}