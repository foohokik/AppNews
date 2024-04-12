package com.example.appnews.data.dataclassesresponse

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


sealed class ArticlesUI {

    @Entity (tableName = "articles")
    data class Article(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        val author: String,
        val content: String,
        val description: String,
        val publishedAt: String,
        val source: Source,
        val title: String,
        val url: String,
        val urlToImage: String
    ): Serializable, ArticlesUI()

    object Loading:ArticlesUI()
}