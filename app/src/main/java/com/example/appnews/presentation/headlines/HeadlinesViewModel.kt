package com.example.appnews.presentation.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.core.shared.ShareDataClass
import com.example.appnews.core.shared.SharedDataType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class HeadlinesViewModel @Inject constructor(private val sharedClass: ShareDataClass) : ViewModel() {

	private val _filterDataFlow = MutableStateFlow(
		SharedDataType.Filter("","","",0)
	)
	val filterDataFlow = _filterDataFlow.asStateFlow()

	fun onWriteData(dataFromFilter: SharedDataType.Filter) {
		sharedClass.setData(dataFromFilter)
		viewModelScope.launch {
			_filterDataFlow.value = dataFromFilter
		}
	}

}