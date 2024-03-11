package com.example.appnews

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.appnews.data.dataclassesresponse.ArticlesUI

import com.example.appnews.presentation.FullArticleFragmentWeb
import com.example.appnews.presentation.headlines.FilterFragment
import com.example.appnews.presentation.headlines.FullArticleHeadlinesFragment
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.headlines.SearchHeadlinesFragment
import com.example.appnews.presentation.navigation.MainFragmentScreen
import com.example.appnews.presentation.navigation.NavigationKeys
import com.example.appnews.presentation.navigation.PagesType
import com.example.appnews.presentation.saves.SaveFragment
import com.example.appnews.presentation.saves.SearchSaveFragment
import com.example.appnews.presentation.source.SearchSourceArticlesFragment
import com.example.appnews.presentation.source.SourceArticlesListFragment
import com.example.appnews.presentation.source.SourceFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

	fun headlinesTab() = object : MainFragmentScreen {
		override val screenKey: String = NavigationKeys.HEADLINES_TAB_FRAGMENT
		override val pagesType: PagesType = PagesType.HEADLINE
		override fun createFragment(factory: FragmentFactory): Fragment {
			return HeadlinesFragment()
		}
	}

	fun saveTab() =  object : MainFragmentScreen {
		override val screenKey: String = NavigationKeys.SAVE_TAB_FRAGMENT
		override val pagesType: PagesType = PagesType.SAVE
		override fun createFragment(factory: FragmentFactory): Fragment {
			return SaveFragment()
		}
	}

	fun sourceTab() =  object : MainFragmentScreen {
		override val screenKey: String = NavigationKeys.SOURCE_TAB_FRAGMENT
		override val pagesType: PagesType = PagesType.SOURCE
		override fun createFragment(factory: FragmentFactory): Fragment {
			return SourceFragment()
		}
	}

	fun fullArticleFragmentWeb(args: ArticlesUI.Article) =
		FragmentScreen { FullArticleFragmentWeb.newInstance(args) }
	fun searchHeadlinesFragment () = FragmentScreen{ SearchHeadlinesFragment()}

	fun searchSaveFragment () = FragmentScreen{ SearchSaveFragment()}

	fun filterFragment () = FragmentScreen { FilterFragment() }
	fun fullArticleHeadlinesFragment (args: ArticlesUI.Article) =
		FragmentScreen { FullArticleHeadlinesFragment.newInstance(args) }


	fun searchSourceFragment (args: String) = FragmentScreen {SearchSourceArticlesFragment.newInstance(args)}

	fun sourceArticlesListFragment (args: String) = FragmentScreen{ SourceArticlesListFragment.newInstance(args) }
}

