package com.example.appnews.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
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


class MainActivity : AppCompatActivity() {

	@Inject
	internal lateinit var viewModelProvider: Provider<MainViewModel>

	@Inject
	lateinit var navigatorHolder: NavigatorHolder

	@Inject
	lateinit var router: Router

	private lateinit var binding: ActivityMainBinding
	private val viewModel by viewModelFactory { viewModelProvider.get() }
	val navigator = MainNavigator(this, R.id.main_container_view)

	override fun onCreate(savedInstanceState: Bundle?) {

		(applicationContext as App).appComponent.inject(this)
		installSplashScreen().setKeepOnScreenCondition() {
			viewModel.isLoading.value
		}

		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)
		setupNavigationBar()
		savedInstanceState ?:initHomeNavBarFragment()
	}

	private fun initHomeNavBarFragment() {

		binding.navBar.selectedItemId = R.id.headline
	}

	private fun setupNavigationBar() {
		binding.navBar.setOnItemSelectedListener {
			when (it.itemId) {
				R.id.headline -> {
					router.replaceScreen(Screens.headlinesTab())
				}

				R.id.saved -> {
					router.replaceScreen(Screens.saveTab())
				}

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
//		bottomNavigationView.setOnNavigationItemSelectedListener(null)
//		bottomNavigationView.getMenu().getItem(pagesType.getValue()).setChecked(true)
//		bottomNavigationView.setOnNavigationItemSelectedListener(this)
	}
}