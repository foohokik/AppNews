package com.example.appnews.presentation.headlines.fullarticle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.presentation.model.ArticlesUI
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.model.FullArticleState
import com.example.appnews.presentation.model.toArticle
import com.github.terrakok.cicerone.Router
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

@RequiresApi(Build.VERSION_CODES.O)
class FullArticleViewModel @AssistedInject constructor(
    private val newsRepository: NewsRepository,
    private val router: Router,
    @Assisted("article") private var article: ArticlesUI.Article?
) : ViewModel() {

    private val _stateIconSaved = MutableStateFlow(false)
    val stateIconSaved = _stateIconSaved.asStateFlow()

    private val _contentState = MutableStateFlow(FullArticleState())
    val contentState = _contentState.asStateFlow()

    init {
        checkArtcicleInDatabase()
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
            cutPublishedDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
        )
        val formatter = DateTimeFormatter.ofPattern("d MMM uuuu | hh-mm a")
        val convertDate = parsedDate.format(formatter)
        _contentState.value = contentState.value.copy(date = convertDate)
    }

    private fun cutPublishedDate(): String? {
        val date = article?.publishedAt?.length ?: 0
        return if (date > 20) {
            article?.publishedAt?.substring(0, 19) + 'Z'
        } else {
            article?.publishedAt
        }
    }

    private fun checkIcon(article: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateIconSaved.value = newsRepository.getArticle(article)
            }
        }
    }

    fun checkArtcicleInDatabase() {
        article?.title?.let { checkIcon(it) }
    }

    private fun saveArticle(article: ArticlesUI.Article) = viewModelScope.launch {
        newsRepository.upsert(article.toArticle())
    }

    private fun deleteArticle(title: String) = viewModelScope.launch {
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

    fun navigateToBack() {
        router.exit()
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("article") article: ArticlesUI.Article?): FullArticleViewModel
    }

}