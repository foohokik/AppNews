package com.example.appnews.presentation.model

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.domain.model.Article
import com.example.appnews.domain.model.News
import com.example.appnews.domain.model.Source
import com.example.appnews.domain.model.Sources


fun Source.toSourceUI(): SourceUI = SourceUI(category, country, description, id, language, name, url)

fun SourceUI.toSource(): Source = Source(category, country, description, id, language, name, url)

fun List<Source>.toListSourcesUI(): List<SourceUI> = this.map { it.toSourceUI() }

fun Article.toArticleUI(): ArticlesUI.Article = ArticlesUI.Article(
    id,
    author,
    content,
    description,
    publishedAt,
    source.toSourceUI(),
    title,
    url,
    urlToImage)

fun ArticlesUI.Article.toArticle(): Article = Article (
    id,
    author,
    content,
    description,
    publishedAt,
    source.toSource(),
    title,
    url,
    urlToImage
)

fun List<Article>.toArticlesUI(): List<ArticlesUI.Article> {
    return this.map { it.toArticleUI() }
}

fun News.toNewsUI(): NewsUI {
    return NewsUI(articles.toArticlesUI(), status, totalResults)
}

fun Sources.toSourcesUI(): SourcesUI = SourcesUI(sources.toListSourcesUI(), status)


fun NetworkResult<News>.toNetworkResultNewsUI(): NetworkResult<NewsUI> {
    return when (this) {
        is NetworkResult.Success<News> -> {
            NetworkResult.Success(this.data.toNewsUI())
        }

        is NetworkResult.Error<News> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}

fun NetworkResult<Sources>.toNetworkResultSourcesUI(): NetworkResult<SourcesUI> {
    return when (this) {
        is NetworkResult.Success<Sources> -> {
            NetworkResult.Success(this.data.toSourcesUI())
        }

        is NetworkResult.Error<Sources> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}
