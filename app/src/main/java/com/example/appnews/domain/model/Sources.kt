package com.example.appnews.domain.model


data class Sources(
    val sources: List<Source> = emptyList(),
    val status: String = ""
)