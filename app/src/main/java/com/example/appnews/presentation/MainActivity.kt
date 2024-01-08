package com.example.appnews.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.databinding.ActivityMainBinding
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val viewModel: MainViewModel by viewModels()
	val navigator = AppNavigator(this, R.id.main_container_view)

	override fun onCreate(savedInstanceState: Bundle?) {

		installSplashScreen().setKeepOnScreenCondition() {
			viewModel.isLoading.value
		}

		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)
		(application as App).router.navigateTo(Screens.headlinesTab())
		setupNavigationBar()
	}

	private fun setupNavigationBar() {
		binding.navBar.setOnItemSelectedListener {
			when (it.itemId) {
				R.id.headline -> (application as App).router.replaceScreen(Screens.headlinesTab())
				R.id.saved -> (application as App).router.replaceScreen(Screens.saveTab())
				R.id.source -> (application as App).router.replaceScreen(Screens.sourceTab())
			}
			true
		}
	}

	override fun onResume() {
		super.onResume()
		(application as App).navigatorHolder.setNavigator(navigator)
	}

	override fun onPause() {
		super.onPause()
		(application as App).navigatorHolder.removeNavigator()
	}
}