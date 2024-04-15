package com.example.appnews.presentation.headlines.tabfragment

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
import com.example.appnews.core.shared.ShareDataClass
import com.example.appnews.core.shared.SharedDataType
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

class PagerContainerViewModel @AssistedInject constructor(
    private val newsRepository: NewsRepository,
    private val sharedClass: ShareDataClass,
    private val networkConnectivityService: NetworkConnectivityService,
    private val router: Router,
    @Assisted("category") private var category: String
) : ViewModel(), ArticleListener {

    private val _networkStatus: MutableStateFlow<NetworkStatus> =
        MutableStateFlow(NetworkStatus.Unknown)
    val networkStatus = _networkStatus

    private val _headlinesNewsFlow = MutableStateFlow(News())
    val headlinesNewsFlow = _headlinesNewsFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _isLastPageFlow = MutableStateFlow<Boolean>(false)
    val isLastPageFlow = _isLastPageFlow.asStateFlow()

    private var headlinesPage = 1
    private var country: String = ""
    private var totalPages = 0

    init {
        if (!networkConnectivityService.isConnected()) {
            _networkStatus.value = NetworkStatus.Disconnected
        }

        viewModelScope.launch {
            networkConnectivityService
                .networkStatus
                .collect { _networkStatus.value = it }
        }
        viewModelScope.launch {
            sharedClass.reviewSearchSideEffect.collect {
                country = (it as SharedDataType.Filter).country
                _isLastPageFlow.value = false
                headlinesPage = 1
                _headlinesNewsFlow.value = _headlinesNewsFlow.value.copy(articles = emptyList())
                getHeadlinesNews()
            }
        }
    }

    fun getHeadlinesNews() {
        viewModelScope.launch {
            if (headlinesPage > 1) {
                _headlinesNewsFlow.value = headlinesNewsFlow.value
                    .copy(articles = headlinesNewsFlow.value.articles + ArticlesUI.Loading)
            }
            val result = newsRepository.getHeadlinesNews(country, category, headlinesPage)

            result.onSuccess { news ->
                _headlinesNewsFlow.value = news.copy(
                    articles = headlinesNewsFlow.value.articles + news.articles
                )
                totalPages = (_headlinesNewsFlow.value.totalResults / PAGE_SIZE) + 1
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException { throwable ->
                _sideEffects.send(SideEffects.ExceptionEffect(throwable))
            }

            _isLastPageFlow.value = (totalPages == headlinesPage)
            if (headlinesPage <= totalPages) {
                headlinesPage++
            }
            _headlinesNewsFlow.value =
                headlinesNewsFlow.value
                    .copy(articles = headlinesNewsFlow.value.articles.filterIsInstance<ArticlesUI.Article>())
        }
    }
    @AssistedFactory
    interface Factory {
        fun create(@Assisted("category") category: String): PagerContainerViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickArticle(article: ArticlesUI.Article) {
        router.navigateTo(
            Screens.fullArticleHeadlinesFragment(article)
        )
    }

}