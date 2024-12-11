package com.example.appnews.presentation.model


data class NewsUI(
    val articles: List<ArticlesUI> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
    )


