package com.example.appnews.presentation.navigation

import com.example.appnews.R

enum class PagesType {
	HEADLINE, SAVE, SOURCE
}

fun PagesType.fromTypeToId(): Int {
	return when(this) {
		PagesType.HEADLINE -> R.id.headline
		PagesType.SAVE -> R.id.saved
		PagesType.SOURCE -> R.id.source
	}
}