package com.example.appnews.presentation.saves

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.presentation.model.ArticlesUI
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.ArticleListener
import com.example.appnews.presentation.model.toArticlesUI
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val router: Router
) : ViewModel(), ArticleListener {

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _articlesFlow = MutableStateFlow(emptyList<ArticlesUI>())
    val articlesFlow  = _articlesFlow.asStateFlow()

    init {
        getArticles()
    }

    fun getArticles() {
        viewModelScope.launch {
            _articlesFlow.value = newsRepository.getAllSavedArticles().toArticlesUI()
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

    fun navigateToSearch () {
        router.navigateTo(Screens.searchSaveFragment())
    }
}