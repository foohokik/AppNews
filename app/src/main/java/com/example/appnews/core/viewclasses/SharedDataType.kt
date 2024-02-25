package com.example.appnews.core.viewclasses

sealed class SharedDataType {
    data class Filter(val country: String): SharedDataType()
}
