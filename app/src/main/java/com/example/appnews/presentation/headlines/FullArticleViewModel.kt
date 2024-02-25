package com.example.appnews.presentation.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullArticleViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _stateIconSaved = MutableStateFlow(false)
    val stateIconSaved = _stateIconSaved.asStateFlow()


    fun saveArticle(article: ArticlesUI.Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }


    fun deleteArticle(title: String) = viewModelScope.launch {
        newsRepository.delete(title)
    }

    fun deleteAll() = viewModelScope.launch {
        newsRepository.deleteAll()
    }

    fun getArticle(title: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateIconSaved.value = newsRepository.getArticle(title)

            }
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

                return FullArticleViewModel(
                    (application as App).newsRepository
                ) as T
            }
        }
    }
}