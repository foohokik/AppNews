package com.example.appnews.presentation.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.core.network.onError
import com.example.appnews.core.network.onException
import com.example.appnews.core.network.onSuccess
import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.ArticleListener
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SourceArticlesListViewModel @AssistedInject constructor(
    private val newsRepository: NewsRepository,
    private val router: Router,
    private val networkConnectivityService: NetworkConnectivityService,
    @Assisted("source") private var sourceId: String
) : ViewModel(), ArticleListener {

    private val _networkStatus: MutableStateFlow<NetworkStatus> = MutableStateFlow(NetworkStatus.Unknown)
    val networkStatus = _networkStatus

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _newsFlow = MutableStateFlow(News())
    val newsFlow = _newsFlow.asStateFlow()

    //private var sourceId: String = ""
    private var headlinesPage = 1


    init {
        if (!networkConnectivityService.isConnected()) {
            _networkStatus.value = NetworkStatus.Disconnected
        }

        viewModelScope.launch {
            networkConnectivityService
                .networkStatus
                .collect { _networkStatus.value = it }
        }
    }


    fun getSourceArticles() {
        viewModelScope.launch {
            val result = newsRepository.getSourcesNews(sourceId)
            result.onSuccess { news ->
                _newsFlow.value = news
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException { throwable ->
                _sideEffects.send(SideEffects.ExceptionEffect(throwable))
            }
        }
    }

    fun navigateToSearch() {
        router.navigateTo(Screens.searchSourceFragment(sourceId))
    }

    override fun onClickArticle(article: ArticlesUI.Article) {
        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickEffectArticle(article))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("source") sourceId: String): SourceArticlesListViewModel
    }

}