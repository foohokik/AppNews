package com.example.appnews.data.model

import com.example.appnews.core.network.NetworkResult
import com.example.appnews.data.database.entity.ArticleEntity
import com.example.appnews.domain.model.Article
import com.example.appnews.domain.model.News
import com.example.appnews.domain.model.Source
import com.example.appnews.domain.model.Sources

fun SourceResponse.toSource(): Source = Source(
    category.orEmpty(),
    country.orEmpty(),
    description.orEmpty(),
    id.orEmpty(),
    language.orEmpty(),
    name.orEmpty(),
    url.orEmpty()
)

fun Source.toSourceResponse(): SourceResponse =
    SourceResponse(category, country, description, id, language, name, url)

fun List<SourceResponse>.toListSources(): List<Source> = this.map { it.toSource() }
fun ArticleResponse.toArticle(): Article {
    return Article(
        id,
        author.orEmpty(),
        content.orEmpty(),
        description.orEmpty(),
        publishedAt,
        source.toSource(),
        title.orEmpty(),
        url,
        urlToImage.orEmpty()
    )
}

fun List<ArticleResponse>.toArticles(): List<Article> {
    return this.map { it.toArticle() }
}

fun NewsResponse.toNews(): News {
    return News(articles.toArticles(), status, totalResults)
}

fun Article.toArticleEntity(): ArticleEntity = ArticleEntity(
    id,
    author,
    content,
    description,
    publishedAt,
    source.toSourceResponse(),
    title,
    url,
    urlToImage
)

fun ArticleEntity.toArticle(): Article = Article(
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

fun List<ArticleEntity>.toListArticle(): List<Article> = this.map { it.toArticle() }

fun List<Article>.toListArticleEntity(): List<ArticleEntity> = this.map { it.toArticleEntity() }

fun SourcesResponse.toSources(): Sources = Sources(sources.toListSources(), status)

fun NetworkResult<NewsResponse>.toNetworkResultNews(): NetworkResult<News> {
    return when (this) {
        is NetworkResult.Success<NewsResponse> -> {
            NetworkResult.Success(this.data.toNews())
        }

        is NetworkResult.Error<NewsResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}

fun NetworkResult<SourcesResponse>.toNetworkResultSources(): NetworkResult<Sources> {
    return when (this) {
        is NetworkResult.Success<SourcesResponse> -> {
            NetworkResult.Success(this.data.toSources())
        }

        is NetworkResult.Error<SourcesResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}

