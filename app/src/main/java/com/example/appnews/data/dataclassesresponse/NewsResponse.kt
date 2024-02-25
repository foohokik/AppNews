package com.example.appnews.data.dataclassesresponse


    data class NewsResponse(
        val articles: List<ArticleResponse> = emptyList(),
        val status: String = "",
        val totalResults: Int = 0
    )




