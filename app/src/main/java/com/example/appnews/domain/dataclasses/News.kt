package com.example.appnews.data.dataclassesresponse

import com.example.appnews.domain.dataclasses.ArticlesUI


data class News(
    val articles: List<ArticlesUI> = emptyList(),
    val status: String = "",
    val totalResults: Int = 0
    )


