package com.example.appnews.presentation.headlines.headlines_adapterRV

import com.example.appnews.domain.dataclasses.ArticlesUI

interface ArticleListener {
    fun onClickArticle (article: ArticlesUI.Article)
}