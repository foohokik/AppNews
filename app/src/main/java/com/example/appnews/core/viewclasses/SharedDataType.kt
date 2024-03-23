package com.example.appnews.core.viewclasses

import java.io.Serializable

sealed class SharedDataType: Serializable {
    data class Filter(
        val country: String,
        val sotrBy: String,
        val date: String,
        val count:Int): SharedDataType()
}
