package com.example.appnews.domain.model

data class News(
    val articles: List<Article> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
    )


