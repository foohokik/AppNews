package com.example.appnews.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CiceroneModule {

    private val cicerone = Cicerone.create()

    @Singleton
    @Provides
    fun router(): Router = cicerone.router

    @Provides
    @Singleton
    fun navigationHolder(): NavigatorHolder = cicerone.getNavigatorHolder()
}