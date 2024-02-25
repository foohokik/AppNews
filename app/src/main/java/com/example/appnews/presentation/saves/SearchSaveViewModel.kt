package com.example.appnews.presentation.saves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.SearchHeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchSaveViewModel(private val newsRepository: NewsRepository): ViewModel(), ArticleListener {


    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchSaveViewModel = MutableStateFlow(News())
    val searchSaveViewModel = _searchSaveViewModel.asStateFlow()


    override fun onClickArticle(article: ArticlesUI.Article) {
        TODO("Not yet implemented")
    }

    companion object {


        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return SearchSaveViewModel(
                    (application as App).newsRepository
                ) as T
            }
        }
    }
}