package com.example.appnews.data.dataclassesresponse


    data class News(
        val articles: List<ArticlesUI> = emptyList(),
        val status: String = "",
        val totalResults: Int = 0
    )


