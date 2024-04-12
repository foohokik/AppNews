package com.example.appnews.presentation.saves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.ArticleListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel(), ArticleListener {

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()



    val getArticles = newsRepository.getAllSavedArticles()
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    override fun onClickArticle(article: ArticlesUI.Article) {
        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickEffectArticle(article))
        }
    }


//    companion object {
//
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                    modelClass: Class<T>,
//                    extras: CreationExtras
//            ): T {
//                val application =
//                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
//
//                return SaveViewModel(
//                        (application as App).newsRepository
//                ) as T
//            }
//        }
//    }

}