package com.example.appnews.di

import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkConnectivityServiceImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Binds
    fun networkConnectivityServiceImplToNetworkConnectivityService (
        networkConnectivityServiceImpl:NetworkConnectivityServiceImpl):NetworkConnectivityService
}