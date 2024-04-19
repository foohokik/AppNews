package com.example.appnews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appnews.domain.dataclasses.ArticlesUI


@Database(
    entities = [ArticlesUI.Article::class],
    version = 2,
    exportSchema = false
         )
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase()  {
    abstract fun getArticleDao(): ArticleDao

}


