package com.example.appnews.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.appnews.data.dataclassesresponse.ArticlesUI
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticlesUI.Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<MutableList<ArticlesUI.Article>>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE title = :title)")
    suspend fun getArticle(title: String): Boolean

    @Query("DELETE from articles WHERE title IN (:title)")
    suspend fun deleteArticle(title: String)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :title || '%' ")
    fun searchArticle(title: String): Flow<List<ArticlesUI.Article>>

}