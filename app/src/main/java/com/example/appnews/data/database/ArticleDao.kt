package com.example.appnews.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appnews.data.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleEntity)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM articles WHERE title = :title)")
    suspend fun getArticle(title: String): Boolean

    @Query("DELETE from articles WHERE title IN (:title)")
    suspend fun deleteArticle(title: String)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :title || '%' ")
    suspend fun searchArticle(title: String): List<ArticleEntity>

}