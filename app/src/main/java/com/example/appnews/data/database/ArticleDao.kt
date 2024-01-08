package com.example.appnews.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appnews.data.dataclasses.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles (): List<Article>  // возможно поменять на LiveData<List<Article>> и убрать suspend

    @Delete
    suspend fun deleteArticle(article: Article)
}