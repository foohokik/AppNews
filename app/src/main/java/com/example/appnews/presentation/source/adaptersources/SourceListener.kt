package com.example.appnews.presentation.source.adaptersources

import com.example.appnews.data.dataclassesresponse.SourceFromSources

interface SourceListener {

    fun onClickSource(source:SourceFromSources)
}