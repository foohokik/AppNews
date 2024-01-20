package com.example.appnews.data.dataclassesresponse

import com.example.appnews.core.DataWrapper


fun ArticleResponse.toArticle(): ArticlesUI.Article {
        return ArticlesUI.Article (id, author.orEmpty(),content.orEmpty(),description.orEmpty(),publishedAt,source,title,url,urlToImage.orEmpty())
    }

   fun List<ArticleResponse>.toArticles(): List<ArticlesUI.Article> {
       return this.map { it.toArticle() }
   }

 fun NewsResponse.toNews(): News {
     return News(articles.toArticles(), status, totalResults)
 }

fun DataWrapper<NewsResponse>.toDataWrapperNews():DataWrapper<News> {

    return DataWrapper<News>(status, data?.toNews())
}

