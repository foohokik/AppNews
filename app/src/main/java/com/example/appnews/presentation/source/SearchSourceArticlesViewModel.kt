package com.example.appnews.presentation.source

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.Status
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.SearchHeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchSourceArticlesViewModel(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ArticleListener {

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchSourceArticles = MutableStateFlow(News())
    val searchSourceArticles = _searchSourceArticles.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var job: Job? = null

    private var sourceId: String = ""

    init {

        sourceId = savedStateHandle[ARG]?:""
    }


    fun changeFlagOnChangeKeyBoardFlag(isShow: Boolean) {
        _showKeyboard.value = isShow
    }

    fun clearFlow() {
        _searchSourceArticles.value.copy(articles = emptyList())
        _queryFlow.value = ""
    }


    fun getSearchNews(searchQuery: String) {

        _queryFlow.value = searchQuery
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            val result = newsRepository.getSearchNewsFromSource(sourceId, searchQuery)
            when (result.status) {
                is Status.Success -> {
                    result.data?.let { news ->
                        _searchSourceArticles.value = news
                    }
                }

                is Status.Error -> {
                    _sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))
                }

                else -> Unit

            }

            _queryFlow.value = searchQuery

        }
    }



    companion object {

        private const val ARG = "ARG"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return SearchSourceArticlesViewModel(
                    (application as App).newsRepository,
                    savedStateHandle
                ) as T
            }
        }

    }


    override fun onClickArticle(article: ArticlesUI.Article) {
        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickEffectArticle(article))
        }
    }
















}