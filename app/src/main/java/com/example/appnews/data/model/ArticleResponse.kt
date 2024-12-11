package com.example.appnews.data.model

import java.io.Serializable


    data class ArticleResponse(
        var id:Long,
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String,
        val source: SourceResponse,
        val title: String?,
        val url: String,
        val urlToImage: String?
    ): Serializable

