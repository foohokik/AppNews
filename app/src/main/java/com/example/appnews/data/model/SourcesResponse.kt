package com.example.appnews.data.model

data class SourcesResponse(
    val sources: List<SourceResponse> = emptyList(),
    val status: String = ""
)