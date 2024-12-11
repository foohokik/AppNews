package com.example.appnews.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.databinding.ActivityMainBinding
import com.example.appnews.presentation.navigation.MainNavigator
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.navigation.PagesType
import com.example.appnews.presentation.navigation.fromTypeToId
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Provider


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    internal lateinit var viewModelProvider: Provider<MainViewModel>

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val binding by viewBinding (ActivityMainBinding::bind)

    private val viewModel by viewModelFactory { viewModelProvider.get() }
    private val navigator = MainNavigator(this, R.id.main_container_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(this)
        installSplashScreen().setKeepOnScreenCondition() {
            viewModel.isLoading.value
        }
        super.onCreate(savedInstanceState)
        setupNavigationBar()
        savedInstanceState ?: initHomeNavBarFragment()
    }

    private fun initHomeNavBarFragment() {
        binding.navBar.selectedItemId = R.id.headline
    }

    private fun setupNavigationBar() {
        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.headline -> router.replaceScreen(Screens.headlinesTab())
                R.id.saved -> router.replaceScreen(Screens.saveTab())
                R.id.source -> router.replaceScreen(Screens.sourceTab())
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        (supportFragmentManager.fragments.findLast { it is OnBackPressedListener }
                as? OnBackPressedListener)
            ?.onBackPressed()
            ?: super.onBackPressed()
    }

    fun setSelectedBottomNavigationTab(pagesType: PagesType?) {
        if (pagesType == null) {
            return
        }
        binding.navBar.selectedItemId = pagesType.fromTypeToId()
    }
}