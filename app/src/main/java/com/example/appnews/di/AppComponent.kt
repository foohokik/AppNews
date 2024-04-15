package com.example.appnews.di

import android.content.Context
import com.example.appnews.presentation.MainActivity
import com.example.appnews.presentation.headlines.filter.FilterFragment
import com.example.appnews.presentation.headlines.fullarticle.FullArticleHeadlinesFragment
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.headlines.search.SearchHeadlinesFragment
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerFragment
import com.example.appnews.presentation.saves.SaveFragment
import com.example.appnews.presentation.saves.SearchSaveFragment
import com.example.appnews.presentation.source.SearchSourceArticlesFragment
import com.example.appnews.presentation.source.SourceArticlesListFragment
import com.example.appnews.presentation.source.SourceFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [
    NetworkModule::class,
    DataBaseModule::class,
    AppModule::class,
    CiceroneModule::class,
    AppBindModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(pagerContainerFragment: PagerContainerFragment)
    fun inject(filterFragment: FilterFragment)
    fun inject(fullArticleHeadlinesFragment: FullArticleHeadlinesFragment)
    fun inject(headlinesFragment: HeadlinesFragment)
    fun inject(searchHeadlinesFragment: SearchHeadlinesFragment)
    fun inject(saveFragment: SaveFragment)
    fun inject(searchSaveFragment: SearchSaveFragment)
    fun inject(searchSourceArticlesFragment: SearchSourceArticlesFragment)
    fun inject(sourceArticlesListFragment: SourceArticlesListFragment)
    fun inject(sourceFragment: SourceFragment)


    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}