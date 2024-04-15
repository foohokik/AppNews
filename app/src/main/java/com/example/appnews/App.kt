package com.example.appnews

import android.app.Application
import com.example.appnews.di.AppComponent
import com.example.appnews.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)

    }

}