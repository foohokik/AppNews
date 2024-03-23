package com.example.appnews.presentation.source.AdapterSources

import com.example.appnews.data.dataclassesresponse.SourceFromSources

interface SourceListener {

    fun onClickSource(source:SourceFromSources)
}