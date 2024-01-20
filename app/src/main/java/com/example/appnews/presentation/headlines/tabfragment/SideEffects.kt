package com.example.appnews.presentation.headlines.tabfragment


import com.example.appnews.data.dataclassesresponse.ArticlesUI

sealed class SideEffects  {

    data class ErrorEffect(val err: String ): SideEffects()
    data class ClickEffect(val article: ArticlesUI.Article): SideEffects()

}
