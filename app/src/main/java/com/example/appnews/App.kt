package com.example.appnews

import android.app.Application
import com.example.appnews.di.AppComponent
import com.example.appnews.di.DaggerAppComponent

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