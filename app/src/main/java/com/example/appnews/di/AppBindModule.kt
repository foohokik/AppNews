package com.example.appnews.di

import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkConnectivityServiceImpl
import com.example.appnews.data.repository.NewsRepositoryImpl
import com.example.appnews.domain.NewsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Binds
    fun networkConnectivityServiceImplToNetworkConnectivityService (
        networkConnectivityService:NetworkConnectivityServiceImpl):NetworkConnectivityService

    @Binds
    fun repoImplToRepo (newsRepository: NewsRepositoryImpl):NewsRepository
}