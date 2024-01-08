package com.example.appnews.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appnews.data.dataclasses.Article
import kotlinx.coroutines.internal.synchronized


@Database(
    entities = [Article::class],
    version = 1
         )
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase()  {

    abstract fun getArticleDao(): ArticleDao



    companion object {
        @Volatile
        private var instance: ArticlesDatabase?= null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance?: kotlin.synchronized(LOCK) {

            instance?: crateDatabase(context).also{instance  = it}

        }

        private fun crateDatabase (context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticlesDatabase::class.java,
                "artcile_db"
            ).build()
    }


    //    companion object {
//        @Volatile
//        private var instance: ArticleDatabase? = null
//
//        fun getInstance(context: Context): ArticleDatabase? {
//            if (instance == null) {
//                synchronized(ArticleDatabase::class) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        ArticleDatabase::class.java, "article.db"
//                    ).build()
//                }
//            }
//            return instance
//        }
//    }

}


