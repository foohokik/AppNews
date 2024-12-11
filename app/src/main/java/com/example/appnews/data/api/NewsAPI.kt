package com.example.appnews.data.api

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.data.model.SourcesResponse
import com.example.appnews.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlinesNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("page") pageNumber: Int
    ): NetworkResult<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("category") category: String,
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int
    ): NetworkResult<NewsResponse>

    @GET("/v2/top-headlines/sources")
    suspend fun getSources(): NetworkResult<SourcesResponse>

    @GET("v2/top-headlines")
    suspend fun getArticlesFromSource(
        @Query("sources") sourceId: String
    ): NetworkResult<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getSearchNewsFromSource(
        @Query("sources") sourceId: String,
        @Query("q") searchQuery: String
    ): NetworkResult<NewsResponse>

}