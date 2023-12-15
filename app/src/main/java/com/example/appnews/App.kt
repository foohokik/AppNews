package com.example.appnews

import android.app.Application
import com.github.terrakok.cicerone.Cicerone

object App: Application() {

    val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

}