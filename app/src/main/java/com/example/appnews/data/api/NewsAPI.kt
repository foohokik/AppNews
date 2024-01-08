package com.example.appnews.data.api

import com.example.appnews.core.API_KEY
import com.example.appnews.data.dataclasses.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlinesNews(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<News>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<News>

}