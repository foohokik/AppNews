package com.example.appnews.data.repository

import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.data.api.RetrofitInstance
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclasses.News

class NewsRepository(
    private val db: ArticlesDatabase,
    private val converter: HttpResultToDataWrapperConverter
) {

    suspend fun getHeadlinesNews(country: String): DataWrapper<News> {
        return converter.convert(RetrofitInstance.api.getHeadlinesNews(country))
    }

}