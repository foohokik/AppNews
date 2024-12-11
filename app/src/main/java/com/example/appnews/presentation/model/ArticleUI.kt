package com.example.appnews.presentation.model

import java.io.Serializable


sealed class ArticlesUI {
    data class Article(
        var id: Long,
        val author: String,
        val content: String,
        val description: String,
        val publishedAt: String,
        val source: SourceUI,
        val title: String,
        val url: String,
        val urlToImage: String
    ): ArticlesUI(), Serializable

    object Loading: ArticlesUI()
}