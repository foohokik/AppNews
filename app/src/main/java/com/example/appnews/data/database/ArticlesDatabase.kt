package com.example.appnews.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appnews.data.dataclassesresponse.ArticlesUI


@Database(
    entities = [ArticlesUI.Article::class],
    version = 2,
    exportSchema = false
         )
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase()  {

    abstract fun getArticleDao(): ArticleDao
//
//    companion object {
//        @Volatile
//        private var instance: ArticlesDatabase?= null
//        private var LOCK = Any()
//
//        operator fun invoke(context: Context) = instance?: kotlin.synchronized(LOCK) {
//
//            instance?: createDatabase(context).also{instance  = it}
//
//        }
//
//        private fun createDatabase (context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                ArticlesDatabase::class.java,
//                "artcile_db"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//    }

}


