package com.example.appnews

import android.app.Application
import android.content.Context
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.repository.NewsRepository
import com.github.terrakok.cicerone.Cicerone

class App: Application() {

    val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    lateinit var articlesDatabase:ArticlesDatabase


    override fun onCreate() {
        super.onCreate()
         articlesDatabase = ArticlesDatabase.invoke(applicationContext)
    }



}