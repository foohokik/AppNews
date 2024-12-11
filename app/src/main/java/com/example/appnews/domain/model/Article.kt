package com.example.appnews.domain.model


    data class Article(
        var id: Long,
        val author: String,
        val content: String,
        val description: String,
        val publishedAt: String,
        val source: Source,
        val title: String,
        val url: String,
        val urlToImage: String
    )