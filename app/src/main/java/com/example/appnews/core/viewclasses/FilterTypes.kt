package com.example.appnews.core.viewclasses

import com.example.appnews.R

enum class FilterTypes(val title: Int, val code:String) {
    POPULAR(R.string.popular_articles, "popular"), NEW(R.string.new_articles, "new"),
    RELEVANT(R.string.relevant_articles, "relevant"), RUSSIAN(R.string.russian, "ru"),
    ENGLISH (R.string.english,"us"), DEUTSCH(R.string.deutsch, "de")
}