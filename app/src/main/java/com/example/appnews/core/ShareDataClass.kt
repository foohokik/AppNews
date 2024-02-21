package com.example.appnews.core

import com.example.appnews.core.viewclasses.SharedDataType
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ShareDataClass {

//    private var _reviewSearchSideEffect = Channel<SharedDataType>()
//    val reviewSearchSideEffect = _reviewSearchSideEffect.receiveAsFlow()
//
//    suspend fun setData(data: SharedDataType) {
//      _reviewSearchSideEffect.send(data)
//    }

    private var _reviewSearchSideEffect = MutableStateFlow<SharedDataType>(SharedDataType.Filter("us"))
    val reviewSearchSideEffect = _reviewSearchSideEffect.asStateFlow()

    suspend fun setData(data: SharedDataType) {
      _reviewSearchSideEffect.emit(data)
   }

}


