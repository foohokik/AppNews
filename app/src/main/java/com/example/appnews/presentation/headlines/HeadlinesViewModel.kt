package com.example.appnews.presentation.headlines

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.Screens
import com.example.appnews.core.shared.ShareDataClass
import com.example.appnews.core.shared.SharedDataType
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class HeadlinesViewModel @Inject constructor(
    private val sharedClass: ShareDataClass,
    private val router: Router
) : ViewModel() {

    private val _filterDataFlow = MutableStateFlow(
        SharedDataType.Filter("", "", "", 0)
    )
    val filterDataFlow = _filterDataFlow.asStateFlow()

    init {

        viewModelScope.launch {
            sharedClass.reviewSearchSideEffect.collect {
                _filterDataFlow.value = it as SharedDataType.Filter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun navigateToFilter() {
        router.navigateTo(Screens.filterFragment())
    }
    fun navigateToSearch(){
        router.navigateTo(Screens.searchHeadlinesFragment())
    }
    fun navigateToBack() {
        router.exit()
    }
}