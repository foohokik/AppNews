package com.example.appnews.di

import com.example.appnews.core.ShareDataClass
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideShareDataClass (): ShareDataClass {
        return ShareDataClass()
    }
}