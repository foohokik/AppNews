package com.example.appnews.presentation.headlines.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.network.onError
import com.example.appnews.core.network.onException
import com.example.appnews.core.network.onSuccess
import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.domain.dataclasses.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.ArticleListener
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchHeadlinesViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val router: Router,
    private val networkConnectivityService: NetworkConnectivityService
) : ViewModel(), ArticleListener {

    private val _networkStatus: MutableStateFlow<NetworkStatus> =
        MutableStateFlow(NetworkStatus.Unknown)
    val networkStatus = _networkStatus

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchHeadlinesViewModel = MutableStateFlow(News())
    val searchHeadlinesViewModel = _searchHeadlinesViewModel.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var job: Job? = null
    private var headlinesPage = 1
    private var category: String = ""
    private var totalPages = 0
    private var isLastPageViewModel = false

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

    fun clearFlowAndOnChangeKeyBoardFlag() {
        _searchHeadlinesViewModel.value =
            _searchHeadlinesViewModel.value.copy(articles = emptyList())
        _queryFlow.value = ""
        _showKeyboard.value = false
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
            val result = newsRepository.getSearchNews(category, searchQuery, headlinesPage)

            result.onSuccess { news ->
                _searchHeadlinesViewModel.value = news
                totalPages = _searchHeadlinesViewModel.value.totalResults / PAGE_SIZE
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException { throwable ->
                _sideEffects.send(SideEffects.ExceptionEffect(throwable))
            }

            if (headlinesPage <= (totalPages + 1)) {
                headlinesPage++
            }

            isLastPageViewModel = (totalPages + 1) == headlinesPage

            _searchHeadlinesViewModel.value =
                searchHeadlinesViewModel.value
                    .copy(articles = searchHeadlinesViewModel.value.articles.filterIsInstance<ArticlesUI.Article>())
        }
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
