package com.example.appnews.data.api

import com.example.appnews.core.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
//                .connectTimeout(1/10, TimeUnit.MINUTES)
//                .writeTimeout(1/10, TimeUnit.MINUTES)
//                .readTimeout(1/10, TimeUnit.MINUTES)
                         .addInterceptor(logging)
                         .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }

            val api by lazy {

                retrofit.create(NewsAPI::class.java)

        }

    }
}