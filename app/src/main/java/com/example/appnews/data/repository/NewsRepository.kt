package com.example.appnews.data.repository

import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.core.network.NetworkResult
import com.example.appnews.data.api.NewsAPI
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.toDataWrapperNews
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(
    private val db: ArticlesDatabase,
    private val converter: HttpResultToDataWrapperConverter,
    private val api: NewsAPI
) {

    suspend fun getHeadlinesNews(
        country: String,
        category: String,
        pageNumber: Int
    ): DataWrapper<News> {
        val result = api.getHeadlinesNews(country, category, pageNumber)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun getSearchNews(
        category: String,
        searchQuery: String,
        pageNumber: Int
    ): DataWrapper<News> {
        val result = api.searchNews(category, searchQuery, pageNumber)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun getSources(): NetworkResult<AllSources> {
        return api.getSources()
    }

    suspend fun getSourcesNews(sourceId: String): DataWrapper<News> {
        val result = api.getArticlesFromSource(sourceId)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun getSearchNewsFromSource(sourceId: String, searchQuery: String): DataWrapper<News> {
        val result = api.getSearchNewsFromSource(sourceId, searchQuery)
        return converter.convert(result).toDataWrapperNews()
    }

    suspend fun upsert(article: ArticlesUI.Article) = db.getArticleDao().upsert(article)

    suspend fun delete(title: String) = db.getArticleDao().deleteArticle(title)

    fun getAllSavedArticles() = db.getArticleDao().getAllArticles()
    suspend fun deleteAll() = db.getArticleDao().deleteAll()
    suspend fun getArticle(title: String) = db.getArticleDao().getArticle(title)
    fun getSearchArticle(title: String) = db.getArticleDao().searchArticle(title)
}