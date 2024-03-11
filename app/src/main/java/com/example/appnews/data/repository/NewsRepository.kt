package com.example.appnews.data.repository

import android.util.Log
import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.data.api.RetrofitInstance
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.toDataWrapperNews
import kotlinx.coroutines.flow.flowOf

class NewsRepository(private val db: ArticlesDatabase, private val converter: HttpResultToDataWrapperConverter) {


    suspend fun getHeadlinesNews(country: String, category: String, pageNumber: Int): DataWrapper<News> {
        val result = RetrofitInstance.api.getHeadlinesNews(country, category, pageNumber)
        return converter.convert(result).toDataWrapperNews()

    }

    suspend fun getSearchNews(category: String, searchQuery: String, pageNumber: Int): DataWrapper<News> {
        val result = RetrofitInstance.api.searchNews(category, searchQuery, pageNumber)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun getSources(): DataWrapper<AllSources> {
        val result = RetrofitInstance.api.getSources()
        return converter.convert(result)
    }

    suspend fun getSourcesNews(sourceId: String): DataWrapper<News> {
        val result = RetrofitInstance.api.getArticlesFromSource(sourceId)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun getSearchNewsFromSource(sourceId: String,searchQuery: String ): DataWrapper<News> {
        val result = RetrofitInstance.api.getSearchNewsFromSource(sourceId, searchQuery)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun upsert(article: ArticlesUI.Article) = db.getArticleDao().upsert(article)

    suspend fun delete(title: String) = db.getArticleDao().deleteArticle(title)

    fun getAllSavedArticles() = db.getArticleDao().getAllArticles()



    suspend fun deleteAll() = db.getArticleDao().deleteAll()

    suspend fun getArticle(title: String) = db.getArticleDao().getArticle(title)



}