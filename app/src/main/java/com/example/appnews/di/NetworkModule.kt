package com.example.appnews.di

import com.example.appnews.core.BASE_URL
import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.data.api.NewsAPI
import com.example.appnews.data.database.NetworkInterceptor
import dagger.Module
import dagger.Provides

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(networkConnectivityService: NetworkConnectivityService): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        val networkInterceptor = NetworkInterceptor(networkConnectivityService)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(networkInterceptor)
        return client.build()

    }


    @Provides
    @Singleton
    fun provideRetrofit (client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    @Provides
    @Singleton

    fun provideApi(retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }


}