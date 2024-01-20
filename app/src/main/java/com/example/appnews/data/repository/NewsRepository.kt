package com.example.appnews.data.repository

import com.example.appnews.core.Category
import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.data.api.RetrofitInstance
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.NewsResponse
import com.example.appnews.data.dataclassesresponse.toDataWrapperNews
import com.example.appnews.data.dataclassesresponse.toNews

class NewsRepository(
	private val db: ArticlesDatabase,
	private val converter: HttpResultToDataWrapperConverter
) {



	private var currentTabCategory: Category = Category.BUSINESS

	fun setCategory(category: Category) {
		currentTabCategory = category
	}

	suspend fun getHeadlinesNews(category: String, pageNumber: Int): DataWrapper<News> {
		val result = RetrofitInstance.api.getHeadlinesNews(category, pageNumber)
		return converter.convert(result).toDataWrapperNews()
	}

}