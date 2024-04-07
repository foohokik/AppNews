package com.example.appnews.presentation.source

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.core.network.onError
import com.example.appnews.core.network.onException
import com.example.appnews.core.network.onSuccess
import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.SourceFromSources
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.source.AdapterSources.SourceListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourceViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel(),
    SourceListener {

    private val _sourceFlow = MutableStateFlow(AllSources())
    val sourceFlow = _sourceFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getSources()
    }

    private fun getSources() {
        viewModelScope.launch {
            val result = newsRepository.getSources()
            result.onSuccess { allSources ->
                _sourceFlow.value = allSources
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException { throwable ->
                Log.i("myTag", "throwable " + throwable)
            }
        }
    }


    override fun onClickSource(source: SourceFromSources) {

        viewModelScope.launch {
            _sideEffects.send(SideEffects.ClickSource(source))
        }
    }

}


