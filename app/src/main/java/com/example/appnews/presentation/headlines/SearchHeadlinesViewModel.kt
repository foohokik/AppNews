package com.example.appnews.presentation.headlines

import android.util.Log
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
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchHeadlinesViewModel (private val newsRepository: NewsRepository,
                                private val savedStateHandle: SavedStateHandle
) : ViewModel(), ArticleListener {


    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchHeadlinesViewModel = MutableStateFlow(News())
    val searchHeadlinesViewModel = _searchHeadlinesViewModel.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    var job: Job? = null
    private var headlinesPage = 1
    private var category: String = ""
     var searchQuery: String =""
    var totalPages = 0
    var isLastPageViewModel = false



    fun changeFlagonChangeKeyBoardFlag (isShow:Boolean) {
      _showKeyboard.value = isShow
    }



    fun getSearchNews(searchQuery: String) {

        _queryFlow.value = searchQuery
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            if (headlinesPage > 1) {
                _searchHeadlinesViewModel.value = searchHeadlinesViewModel.value
                    .copy(articles = searchHeadlinesViewModel.value.articles + ArticlesUI.Loading)
            }

            _queryFlow.value = searchQuery
            val result = newsRepository.getSearchNews(category,searchQuery, headlinesPage)

            when (result.status) {
                is Status.Success -> {
                    result.data?.let { news ->
                        _searchHeadlinesViewModel.value = news
                    }


                    totalPages = _searchHeadlinesViewModel.value.totalResults / PAGE_SIZE


                }

                is Status.Error -> {
                    _sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))
                }

                else -> Unit

            }

            if (headlinesPage <= (totalPages+1)) {
                headlinesPage++
            }

            isLastPageViewModel = (totalPages+1) == headlinesPage

            _searchHeadlinesViewModel.value =
                searchHeadlinesViewModel.value
                    .copy(articles = searchHeadlinesViewModel.value.articles.filterIsInstance<ArticlesUI.Article>())


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

                val savedStateHandle = extras.createSavedStateHandle()

                return SearchHeadlinesViewModel(
                    (application as App).newsRepository,
                    savedStateHandle
                ) as T
            }
        }
    }




        override fun onClickArticle(article: ArticlesUI.Article) {
            viewModelScope.launch {
                _sideEffects.send(SideEffects.ClickEffect(article))
            }
        }


    }
