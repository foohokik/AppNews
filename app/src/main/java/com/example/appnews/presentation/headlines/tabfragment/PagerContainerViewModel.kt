package com.example.appnews.presentation.headlines.tabfragment

import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.ShareDataClass
import com.example.appnews.core.Status

import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.ArticleListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PagerContainerViewModel(
	private val newsRepository: NewsRepository,
	private val savedStateHandle: SavedStateHandle,
	val sharedClass: ShareDataClass
) : ViewModel(), ArticleListener {


	private val _headlinesNewsFlow = MutableStateFlow(News())
	val headlinesNewsFlow = _headlinesNewsFlow.asStateFlow()


	private val _sideEffects = Channel<SideEffects>()
	val sideEffects = _sideEffects.receiveAsFlow()

	private var headlinesPage = 1
	private var category: String = ""
	var country:String = "us"
	var totalPages = 0
	var isLastPageViewModel = false

	init {

		category = savedStateHandle.get<String>(CATEGORY).orEmpty()
		getHeadlinesNews()

	}


	fun getHeadlinesNews() {
		viewModelScope.launch {

			if (headlinesPage > 1) {
				_headlinesNewsFlow.value = headlinesNewsFlow.value
					.copy(articles = headlinesNewsFlow.value.articles + ArticlesUI.Loading)
			}


			val result = newsRepository.getHeadlinesNews(country, category, headlinesPage)
			Log.d("SUKA", "pagercontainerviewmodel country  " + country +"  category" + category)
			when (result.status) {
				is Status.Success -> {
					result.data?.let { news ->

							_headlinesNewsFlow.value = news.copy(
								articles = headlinesNewsFlow.value.articles
										+ news.articles
							)
						}




					totalPages = _headlinesNewsFlow.value.totalResults / PAGE_SIZE


				}

				is Status.Error -> {
					_sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))
					Log.d("SUKA", "pagercontainerviewmodel error message  " + result.status)
				}

				else -> Unit

			}

			if (headlinesPage <= (totalPages+1)) {
				headlinesPage++
			}

			isLastPageViewModel = (totalPages+1) == headlinesPage

			_headlinesNewsFlow.value =
				headlinesNewsFlow.value
					.copy(articles = headlinesNewsFlow.value.articles.filterIsInstance<ArticlesUI.Article>())


		}
	}

	fun getRenewedHeadlinesNews() {

		_headlinesNewsFlow.value = _headlinesNewsFlow.value.copy(articles = emptyList())

		viewModelScope.launch {

			Log.d("RENEW", "_headlinesNewsFlow.value " + _headlinesNewsFlow.value)
			if (headlinesPage > 1) {
				_headlinesNewsFlow.value = headlinesNewsFlow.value
					.copy(articles = headlinesNewsFlow.value.articles + ArticlesUI.Loading)
			}


			val result = newsRepository.getHeadlinesNews(country, category, headlinesPage)

			when (result.status) {
				is Status.Success -> {
					result.data?.let { news ->

						_headlinesNewsFlow.value = news.copy(
							articles = headlinesNewsFlow.value.articles
									+ news.articles
						)

					}




					totalPages = _headlinesNewsFlow.value.totalResults / PAGE_SIZE


				}

				is Status.Error -> {
					_sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))
					Log.d("SUKA", "pagercontainerviewmodel error message  " + result.status)
				}

				else -> Unit

			}

			if (headlinesPage <= (totalPages+1)) {
				headlinesPage++
			}

			isLastPageViewModel = (totalPages+1) == headlinesPage

			_headlinesNewsFlow.value =
				headlinesNewsFlow.value
					.copy(articles = headlinesNewsFlow.value.articles.filterIsInstance<ArticlesUI.Article>())


		}
	}


	companion object {
		const val CATEGORY = "category"

		val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(
				modelClass: Class<T>,
				extras: CreationExtras
			): T {
				val application =
					checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

				val savedStateHandle = extras.createSavedStateHandle()

				return PagerContainerViewModel(
					(application as App).newsRepository,
					savedStateHandle, (application as App).sharedClass
				) as T
			}
		}
	}


	override fun onClickArticle(article: ArticlesUI.Article) {

		viewModelScope.launch {
			_sideEffects.send(SideEffects.ClickEffect(article))
		}
	}
}