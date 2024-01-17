package com.example.appnews.presentation.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.Category
import com.example.appnews.data.dataclasses.News
import com.example.appnews.data.repository.NewsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


class HeadlinesViewModel(private val newsRepository: NewsRepository) : ViewModel() {

	private val _headlinesNewsFlow = MutableStateFlow(News())
	val headlinesNewsFlow = _headlinesNewsFlow.asStateFlow()

	private val _effects = Channel<String>()
	val effects = _effects.receiveAsFlow()


	fun setCurrentTab(position: Int) {
		newsRepository.setCategory(Category.values()[position])
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

				return HeadlinesViewModel(
					(application as App).newsRepository
				) as T
			}
		}
	}

}