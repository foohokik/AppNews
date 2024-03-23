package com.example.appnews.presentation.headlines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.Category
import com.example.appnews.core.ShareDataClass
import com.example.appnews.core.viewclasses.SharedDataType
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.repository.NewsRepository
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class HeadlinesViewModel(private val sharedClass: ShareDataClass, private val router: Router) : ViewModel() {

	private val _filterDataFlow = MutableStateFlow(
		SharedDataType.Filter("","","",0)
	)
	val filterDataFlow = _filterDataFlow.asStateFlow()

	private val _effects = Channel<String>()
	val effects = _effects.receiveAsFlow()


	fun onWriteData(dataFromFilter:SharedDataType.Filter) {
		sharedClass.setData(dataFromFilter)
		viewModelScope.launch {
			_filterDataFlow.value = dataFromFilter
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

				return HeadlinesViewModel(
					(application as App).sharedClass, (application as App).router
				) as T
			}
		}
	}

}