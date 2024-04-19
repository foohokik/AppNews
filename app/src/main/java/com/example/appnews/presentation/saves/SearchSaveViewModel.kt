package com.example.appnews.presentation.saves

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.domain.dataclasses.ArticlesUI
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.ArticleListener
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchSaveViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val router: Router
): ViewModel(), ArticleListener {

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    private val _showKeyboard = MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _searchSaveViewModel = MutableStateFlow(emptyList<ArticlesUI.Article>())
    val searchSaveViewModel = _searchSaveViewModel.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var job: Job? = null

    fun getSearchSavedArticles(searchQuery: String) {
        _queryFlow.value = searchQuery
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            newsRepository.getSearchSavedArticle(searchQuery)
                .onEach { answer ->
                    _searchSaveViewModel.value = answer
                }
                .launchIn(viewModelScope)
            _queryFlow.value = searchQuery
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

    fun clearFlowAndOnChangeKeyBoardFlag() {
        _searchSaveViewModel.value = emptyList()
        _queryFlow.value = ""
        _showKeyboard.value = false
    }

}