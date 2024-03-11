package com.example.appnews.presentation.headlines.tabfragment


import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.ArticleResponse
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.SourceFromSources

sealed class SideEffects  {

    data class ErrorEffect(val err: String ): SideEffects()
    data class ClickSource(val source:SourceFromSources): SideEffects()

    data class  ClickEffectArticle (val article: ArticlesUI.Article): SideEffects()

}
