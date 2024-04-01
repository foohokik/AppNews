package com.example.appnews.presentation.headlines

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.dataclasses.FullArticleState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class FullArticleViewModel @AssistedInject constructor(
    private val newsRepository: NewsRepository,
    @Assisted("article") private var article: ArticlesUI.Article?
) : ViewModel() {

    private val _stateIconSaved = MutableStateFlow(false)
    val stateIconSaved = _stateIconSaved.asStateFlow()

    private val _contentState = MutableStateFlow(FullArticleState())
    val contentState = _contentState.asStateFlow()

    //private var article: ArticlesUI.Article? = null

    init {
       // article = savedStateHandle[ARG]
        article?.title?.let { checkIcon(it) }
        convertArticleToScreenState()
        initDateAndTime()
    }

    private fun convertArticleToScreenState() {
        article?.let { articleUi ->
            _contentState.value = contentState.value.copy(
                urlImage = articleUi.urlToImage,
                title = articleUi.title,
                description = articleUi.description,
                nameSource = articleUi.source.name,
            )
        }
    }


    private fun initDateAndTime() {
        val parsedDate = LocalDateTime.parse(
            article?.publishedAt,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
        )
        val formatter = DateTimeFormatter.ofPattern("d MMM uuuu | hh-mm a")
        val convertDate = parsedDate.format(formatter)
        _contentState.value = contentState.value.copy(date = convertDate)
    }

    private fun checkIcon(article: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateIconSaved.value = newsRepository.getArticle(article)
            }
        }
    }

    fun saveArticle(article: ArticlesUI.Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }


    fun deleteArticle(title: String) = viewModelScope.launch {
        newsRepository.delete(title)
    }

    fun onSaveOrRemoveArticle() {
        val isSavedArticle = stateIconSaved.value
        article?.let { articleUi ->
            if (isSavedArticle) {
                deleteArticle(articleUi.title)
            } else {
                saveArticle(articleUi)
            }
        }
        _stateIconSaved.value = !isSavedArticle
    }


    @AssistedFactory
    interface Factory {
        fun create(@Assisted("article") article: ArticlesUI.Article?): FullArticleViewModel}

//    companion object {
//
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
//
//                val savedStateHandle = extras.createSavedStateHandle()
//
//                return FullArticleViewModel(
//                    (application as App).newsRepository,
//                    savedStateHandle
//                ) as T
//            }
//        }
//    }
}