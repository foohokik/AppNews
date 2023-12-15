package com.example.appnews.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.appnews.App.navigatorHolder
import com.example.appnews.App.router
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen().setKeepOnScreenCondition() {
            viewModel.isLoading.value
        }

        setupNavigationBar()


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
}