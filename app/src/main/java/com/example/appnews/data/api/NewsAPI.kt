package com.example.appnews.data.api

import com.example.appnews.core.API_KEY
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlinesNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("category") category: String,
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

}