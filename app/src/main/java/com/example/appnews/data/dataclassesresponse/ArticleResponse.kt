package com.example.appnews.data.dataclassesresponse

import com.example.appnews.domain.dataclasses.Source
import java.io.Serializable


    data class ArticleResponse(
        var id:Long,
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String,
        val source: Source,
        val title: String?,
        val url: String,
        val urlToImage: String?
    ): Serializable

