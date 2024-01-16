package com.example.appnews.data.repository

import android.util.Log
import com.example.appnews.core.Category
import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.data.api.RetrofitInstance
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclasses.News

class NewsRepository(
    private val db: ArticlesDatabase,
    private val converter: HttpResultToDataWrapperConverter
) {

    private var currentTabCategory: Category = Category.BUSINESS

    fun setCategory(category: Category) {
        currentTabCategory = category
    }

    suspend fun getHeadlinesNews(): DataWrapper<News> {
        Log.d("LOG", "tyt "+currentTabCategory)
        return converter.convert(RetrofitInstance.api.getHeadlinesNews(currentTabCategory.category))
    }

}