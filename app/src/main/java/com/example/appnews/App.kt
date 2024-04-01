package com.example.appnews

import android.app.Application
import android.content.Context
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.core.ShareDataClass
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.di.AppComponent
import com.example.appnews.di.DaggerAppComponent
import com.github.terrakok.cicerone.Cicerone

class App: Application() {

//    val cicerone = Cicerone.create()
//    val router get() = cicerone.router
//    val navigatorHolder get() = cicerone.getNavigatorHolder()

//    lateinit var articlesDatabase:ArticlesDatabase
//    lateinit var newsRepository: NewsRepository
//    lateinit var sharedClass: ShareDataClass

    lateinit var appComponent: AppComponent



    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)

//         sharedClass = ShareDataClass()
//         articlesDatabase = ArticlesDatabase.invoke(applicationContext)
//         newsRepository = NewsRepository(articlesDatabase, HttpResultToDataWrapperConverter())
    }



}