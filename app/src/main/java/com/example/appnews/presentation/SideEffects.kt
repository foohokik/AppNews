package com.example.appnews.presentation


import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.SourceFromSources

sealed class SideEffects  {

    data class ErrorEffect(val err: String): SideEffects()
    data class ExceptionEffect(val throwable: Throwable): SideEffects()
    data class ClickSource(val source:SourceFromSources): SideEffects()
    data class  ClickEffectArticle (val article: ArticlesUI.Article): SideEffects()

}
