package com.example.appnews.presentation.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.core.network.onError
import com.example.appnews.core.network.onException
import com.example.appnews.core.network.onSuccess
import com.example.appnews.core.networkstatus.NetworkConnectivityService
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.domain.NewsRepository
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.model.SourceUI
import com.example.appnews.presentation.model.SourcesUI
import com.example.appnews.presentation.model.toSourcesUI
import com.example.appnews.presentation.source.adaptersources.SourceListener
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourceViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkConnectivityService: NetworkConnectivityService,
    private val router: Router
) : ViewModel(), SourceListener {

    private val _networkStatus: MutableStateFlow<NetworkStatus> =
        MutableStateFlow(NetworkStatus.Unknown)
    val networkStatus = _networkStatus

    private val _sourceFlow = MutableStateFlow(SourcesUI())
    val sourceFlow = _sourceFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        if (!networkConnectivityService.isConnected()) {
            _networkStatus.value = NetworkStatus.Disconnected
        }
        initNetworkStatus()
    }

    private fun initNetworkStatus() {
        viewModelScope.launch {
            networkConnectivityService
                .networkStatus
                .collect { _networkStatus.value = it }
        }
    }

    fun getSources() {
        viewModelScope.launch {
            val result = newsRepository.getSources()
            result.onSuccess { allSources ->
                _sourceFlow.value = allSources.toSourcesUI()
            }.onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
            }.onException {
                _sideEffects.send(SideEffects.ExceptionEffect(it))
            }
        }
    }

    fun navigateToBack() {
        router.exit()
    }

    override fun onClickSource(source: SourceUI) {
        router.navigateTo(
            Screens.sourceArticlesListFragment(source.id)
        )
    }

}


