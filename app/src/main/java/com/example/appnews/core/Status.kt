package com.example.appnews.core

sealed class Status {
    object Success : Status()

    data class Error(
        val message: String? = null
    ) : Status()
}
