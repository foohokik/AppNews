package com.example.appnews.data.dataclassesresponse

data class AllSources(
    val sources: List<SourceFromSources> = emptyList(),
    val status: String = ""
)