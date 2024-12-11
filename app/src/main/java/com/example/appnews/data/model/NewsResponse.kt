package com.example.appnews.data.model


    data class NewsResponse(
        val articles: List<ArticleResponse> = emptyList(),
        val status: String = "",
        val totalResults: Int = 0
    )




