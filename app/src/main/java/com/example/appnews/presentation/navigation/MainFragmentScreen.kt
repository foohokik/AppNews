package com.example.appnews.presentation.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface MainFragmentScreen : FragmentScreen {
	val pagesType: PagesType
}