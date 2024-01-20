package com.example.appnews.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.appnews.data.dataclassesresponse.ArticlesUI


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticlesUI.Article): Long

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles (): List<ArticlesUI.Article>  // возможно поменять на LiveData<List<Article>> и убрать suspend

    @Delete
    suspend fun deleteArticle(article: ArticlesUI.Article)
}