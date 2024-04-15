package com.example.appnews.data.dataclassesresponse

import com.example.appnews.core.network.NetworkResult


fun ArticleResponse.toArticle(): ArticlesUI.Article {
        return ArticlesUI.Article (id, author.orEmpty(),content.orEmpty(),description.orEmpty(),publishedAt,source, title.orEmpty(),url,urlToImage.orEmpty())
    }

   fun List<ArticleResponse>.toArticles(): List<ArticlesUI.Article> {
       return this.map { it.toArticle() }
   }

 fun NewsResponse.toNews(): News {
     return News(articles.toArticles(), status, totalResults)
 }

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

