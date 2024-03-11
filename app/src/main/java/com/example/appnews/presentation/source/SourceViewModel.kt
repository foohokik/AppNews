package com.example.appnews.presentation.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.Status
import com.example.appnews.data.dataclassesresponse.AllSources
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.SourceFromSources
import com.example.appnews.data.repository.NewsRepository
import com.example.appnews.presentation.headlines.FilterViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.source.AdapterSources.SourceListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SourceViewModel(private val newsRepository: NewsRepository) : ViewModel(), SourceListener {

    private val _sourceFlow = MutableStateFlow(AllSources())
    val sourceFlow = _sourceFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getSources()
    }

    fun getSources() {

        viewModelScope.launch {
            val result = newsRepository.getSources()

            when (result.status) {
                is Status.Success -> {
                    result.data?.let { sources ->

                        _sourceFlow.value = sources

                    }
                }

                is Status.Error -> {
                    _sideEffects.send(SideEffects.ErrorEffect(result.status.message.orEmpty()))

                }

                else -> Unit
            }

        }
    }


        override fun onClickSource(source: SourceFromSources) {

            viewModelScope.launch {
                _sideEffects.send(SideEffects.ClickSource(source))
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
                    extras.createSavedStateHandle()

                    return SourceViewModel(
                        (application as App).newsRepository
                    ) as T
                }
            }
        }
    }


