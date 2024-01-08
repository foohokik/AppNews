package com.example.appnews.data.dataclasses

data class News(
    val articles: List<Article> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
)