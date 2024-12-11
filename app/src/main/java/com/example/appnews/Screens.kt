package com.example.appnews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.appnews.presentation.model.ArticlesUI
import com.example.appnews.presentation.headlines.filter.FilterFragment
import com.example.appnews.presentation.headlines.fullarticle.FullArticleHeadlinesFragment
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.headlines.search.SearchHeadlinesFragment
import com.example.appnews.presentation.navigation.MainFragmentScreen
import com.example.appnews.presentation.navigation.NavigationKeys
import com.example.appnews.presentation.navigation.PagesType
import com.example.appnews.presentation.saves.SaveFragment
import com.example.appnews.presentation.saves.SearchSaveFragment
import com.example.appnews.presentation.source.search_articles.SearchSourceArticlesFragment
import com.example.appnews.presentation.source.articles.SourceArticlesListFragment
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

	fun searchHeadlinesFragment () = FragmentScreen{ SearchHeadlinesFragment() }

	fun searchSaveFragment () = FragmentScreen{ SearchSaveFragment()}

	@RequiresApi(Build.VERSION_CODES.O)
	fun filterFragment () = FragmentScreen { FilterFragment() }
	@RequiresApi(Build.VERSION_CODES.O)
	fun fullArticleHeadlinesFragment (args: ArticlesUI.Article) =
		FragmentScreen { FullArticleHeadlinesFragment.newInstance(args) }


	fun searchSourceFragment (args: String) = FragmentScreen { SearchSourceArticlesFragment.newInstance(args)}

	fun sourceArticlesListFragment (args: String) = FragmentScreen{ SourceArticlesListFragment.newInstance(args) }
}

