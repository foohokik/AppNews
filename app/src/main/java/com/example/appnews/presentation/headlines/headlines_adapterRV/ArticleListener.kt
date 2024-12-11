package com.example.appnews.presentation.headlines.headlines_adapterRV

import com.example.appnews.presentation.model.ArticlesUI

interface ArticleListener {
    fun onClickArticle (article: ArticlesUI.Article)
}