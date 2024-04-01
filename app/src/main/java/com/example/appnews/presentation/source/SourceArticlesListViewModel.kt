package com.example.appnews.presentation.source

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.Status
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.FullArticleViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
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
    @Assisted("source") private var sourceId: String
) : ViewModel(), ArticleListener {


    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _newsFlow = MutableStateFlow(News())
    val newsFlow = _newsFlow.asStateFlow()

    //private var sourceId: String = ""
    private var headlinesPage = 1


    init {
        getSourceArticles()
     //   sourceId = savedStateHandle[ARG]?: "" getSourceArticles()
    }


    fun getSourceArticles() {
        viewModelScope.launch {
            val result = newsRepository.getSourcesNews(sourceId)
            when (result.status) {
                is Status.Success -> {
                    result.data?.let { news ->
                        _newsFlow.value = news
                    }
                }
                is Status.Error -> {
                    _sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))
                }
                else -> Unit
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


//    companion object {
//        private const val ARG = "ARG"
//
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                val application =
//                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
//                val savedStateHandle = extras.createSavedStateHandle()
//
//                return SourceArticlesListViewModel(
//                    (application as App).newsRepository,
//                    savedStateHandle, (application as App).router
//                ) as T
//            }
//        }
//    }
}