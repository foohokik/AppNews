package com.example.appnews.presentation.headlines.tabfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.core.Status
import com.example.appnews.data.dataclasses.Article
import com.example.appnews.data.dataclasses.News
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PagerContainerViewModel(private val newsRepository: NewsRepository) : ViewModel(), ArticleListener {

    private val _headlinesNewsFlow = MutableStateFlow(News())
    val headlinesNewsFlow = _headlinesNewsFlow.asStateFlow()


    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()


    init {
        viewModelScope.launch {
            val result = getHeadlinesNews()
            when (result.status) {
                is Status.Success -> {
                    result.data?.let {
                        _headlinesNewsFlow.value = it
                    }
                }

                is Status.Error -> {
                    _sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()) )
                }

                else -> Unit
            }
        }
    }


    private suspend fun getHeadlinesNews(): DataWrapper<News> {
        return newsRepository.getHeadlinesNews(COUNTRY_INDEX)
    }


    companion object {
        private const val COUNTRY_INDEX = "us"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return PagerContainerViewModel(
                    NewsRepository(
                        (application as App).articlesDatabase,
                        HttpResultToDataWrapperConverter()
                    )
                ) as T
            }
        }
    }



    override fun onClickArticle(article: Article) {

        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickEffect(article))
        }



    }
}