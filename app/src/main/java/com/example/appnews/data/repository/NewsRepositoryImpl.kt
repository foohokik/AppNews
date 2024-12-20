package com.example.appnews.data.repository

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.data.api.NewsAPI
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.model.toArticleEntity
import com.example.appnews.data.model.toListArticle
import com.example.appnews.data.model.toNetworkResultNews
import com.example.appnews.data.model.toNetworkResultSources
import com.example.appnews.domain.NewsRepository
import com.example.appnews.domain.model.Article
import com.example.appnews.domain.model.News
import com.example.appnews.domain.model.Sources
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val db: ArticlesDatabase,
    private val api: NewsAPI
):NewsRepository {
    override suspend fun getHeadlinesNews(
        country: String,
        category: String,
        pageNumber: Int
    ): NetworkResult<News> {
        val result = api.getHeadlinesNews(country, category, pageNumber)
        return result.toNetworkResultNews()
    }

    override suspend fun getSearchNews(
        category: String,
        searchQuery: String,
        pageNumber: Int
    ): NetworkResult<News> {
        val result = api.searchNews(category, searchQuery, pageNumber)
        return result.toNetworkResultNews()
    }

    override suspend fun getSources(): NetworkResult<Sources> {
        return api.getSources().toNetworkResultSources()
    }

    override suspend fun getSourcesNews(sourceId: String): NetworkResult<News> {
        val result = api.getArticlesFromSource(sourceId)
        return result.toNetworkResultNews()
    }

    override suspend fun getSearchNewsFromSource(sourceId: String, searchQuery: String): NetworkResult<News> {
        val result = api.getSearchNewsFromSource(sourceId, searchQuery)
        return result.toNetworkResultNews()
    }
    override suspend fun upsert(article: Article) = db.getArticleDao().upsert(article.toArticleEntity())
    override suspend fun delete(title: String) = db.getArticleDao().deleteArticle(title)
    override suspend fun getAllSavedArticles() = db.getArticleDao().getAllArticles().toListArticle()
    override suspend fun deleteAll() = db.getArticleDao().deleteAll()
    override suspend fun getArticle(title: String) = db.getArticleDao().getArticle(title)
    override suspend fun getSearchSavedArticle(title: String) = db.getArticleDao().searchArticle(title).toListArticle()
}