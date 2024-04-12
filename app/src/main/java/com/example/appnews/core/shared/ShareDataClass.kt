package com.example.appnews.core.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShareDataClass {

        private val _reviewSearchSideEffect = MutableStateFlow<SharedDataType>(
            SharedDataType.Filter("us", "", "", 0)
        )
        val reviewSearchSideEffect = _reviewSearchSideEffect.asStateFlow()

         fun setData(data: SharedDataType) {
          _reviewSearchSideEffect.value = data
       }

    }


