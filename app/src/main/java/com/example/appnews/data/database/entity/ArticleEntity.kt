package com.example.appnews.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appnews.data.model.SourceResponse

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceResponse,
    val title: String,
    val url: String,
    val urlToImage: String
)
