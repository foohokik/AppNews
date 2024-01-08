package com.example.appnews

import com.example.appnews.data.dataclasses.Article
import com.example.appnews.presentation.FullArticleFragment
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.saves.SaveFragment
import com.example.appnews.presentation.source.SourceFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import java.io.Serializable

object Screens {

    fun headlinesTab () = FragmentScreen {HeadlinesFragment()}
    fun saveTab () = FragmentScreen {SaveFragment()}
    fun sourceTab () = FragmentScreen { SourceFragment() }

    fun fullArticleFragment (args: Article) = FragmentScreen {FullArticleFragment.newInstance(args)}

}