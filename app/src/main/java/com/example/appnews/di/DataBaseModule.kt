package com.example.appnews.di

import android.content.Context
import androidx.room.Room
import com.example.appnews.data.database.ArticleDao
import com.example.appnews.data.database.ArticlesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataBaseModule {


     @Provides
     @Singleton
     fun provideDatabase(context: Context): ArticlesDatabase{
      return   Room.databaseBuilder(
            context,
            ArticlesDatabase::class.java,
            "artcile_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGetArticleDao(db:ArticlesDatabase): ArticleDao {
        return  db.getArticleDao()
    }

}

