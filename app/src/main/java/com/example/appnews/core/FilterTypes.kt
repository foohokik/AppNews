package com.example.appnews.core

import com.example.appnews.R

enum class FilterTypes(val title: Int, val code:String="") {
    POPULAR(R.string.popular_articles), NEW(R.string.new_articles),
    RELEVANT(R.string.relevant_articles), RUSSIAN(R.string.russian, "ru"),
    ENGLISH (R.string.english,"us"), DEUTSCH(R.string.deutsch, "de")
}