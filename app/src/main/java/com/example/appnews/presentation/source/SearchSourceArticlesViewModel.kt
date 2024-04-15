package com.example.appnews.presentation.source

import android.os.Build
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchSourceArticlesViewModel @AssistedInject constructor(
    private val newsRepository: NewsRepository,
    private val networkConnectivityService: NetworkConnectivityService,
    @Assisted("source") private var sourceId: String,
    private val router: Router
) : ViewModel(), ArticleListener {

    private val _networkStatus: MutableStateFlow<NetworkStatus> = MutableStateFlow(NetworkStatus.Unknown)
    val networkStatus = _networkStatus

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchSourceArticles = MutableStateFlow(News())
    val searchSourceArticles = _searchSourceArticles.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var job: Job? = null
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
            result.onSuccess { news ->
                _searchSourceArticles.value = news
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException { throwable ->
                _sideEffects.send(SideEffects.ExceptionEffect(throwable))
            }
            _queryFlow.value = searchQuery
        }
    }
    @AssistedFactory
    interface Factory {
        fun create(@Assisted("source") sourceId: String): SearchSourceArticlesViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickArticle(article: ArticlesUI.Article) {
        router.navigateTo(
            Screens.fullArticleHeadlinesFragment(article)
        )
    }
    fun navigateToBack() {
        router.exit()
    }

}