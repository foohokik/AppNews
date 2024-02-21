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
    suspend fun upsert(article: ArticlesUI.Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles (): Flow<List<ArticlesUI.Article>> // возможно поменять на LiveData<List<Article>> и убрать suspend
   // StateFlow<List<ArticlesUI.Article>>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE url = :url)")
    suspend fun getArticle(url: String): Boolean

//    @Query("DELETE from articles WHERE id IN (:id)")
//    suspend fun deleteArticle(id: Long)
    @Delete
    suspend fun deleteArticle(article: ArticlesUI.Article)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()
}