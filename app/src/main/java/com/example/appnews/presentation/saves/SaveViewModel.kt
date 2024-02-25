package com.example.appnews.presentation.saves

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.HeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SaveViewModel(private val newsRepository: NewsRepository) : ViewModel(), ArticleListener {

    private val _savedArticleFlow = MutableStateFlow(emptyList<ArticlesUI.Article>())
    val savedArticleFlow = _savedArticleFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()



    val getArticles = newsRepository.getAllSavedArticles()
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    override fun onClickArticle(article: ArticlesUI.Article) {
        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickEffect(article))
        }
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

                return SaveViewModel(
                        (application as App).newsRepository
                ) as T
            }
        }
    }

}