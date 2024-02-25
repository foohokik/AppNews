package com.example.appnews

import android.app.Application
import android.content.Context
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.core.ShareDataClass
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.repository.NewsRepository
import com.github.terrakok.cicerone.Cicerone

class App: Application() {

    val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    lateinit var articlesDatabase:ArticlesDatabase
    lateinit var newsRepository: NewsRepository
    lateinit var sharedClass: ShareDataClass




    override fun onCreate() {
        super.onCreate()
         sharedClass = ShareDataClass()
         articlesDatabase = ArticlesDatabase.invoke(applicationContext)
         newsRepository = NewsRepository(articlesDatabase, HttpResultToDataWrapperConverter())
    }



}