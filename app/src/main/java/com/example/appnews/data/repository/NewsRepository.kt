package com.example.appnews.data.repository

import android.util.Log
import com.example.appnews.core.DataWrapper
import com.example.appnews.core.HttpResultToDataWrapperConverter
import com.example.appnews.data.api.RetrofitInstance
import com.example.appnews.data.database.ArticlesDatabase
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.data.dataclassesresponse.News
import com.example.appnews.data.dataclassesresponse.toDataWrapperNews

class NewsRepository(
	private val db: ArticlesDatabase,
	private val converter: HttpResultToDataWrapperConverter
) {

//
//	private var currentTabCategory: Category = Category.BUSINESS
//
//	fun setCategory(category: Category) {
//		currentTabCategory = category
//	}

	suspend fun getHeadlinesNews(country:String, category: String, pageNumber: Int): DataWrapper<News> {
		val result = RetrofitInstance.api.getHeadlinesNews(country, category, pageNumber)
		return converter.convert(result).toDataWrapperNews()

	}

	suspend fun getSearchNews(category: String, searchQuery: String, pageNumber: Int): DataWrapper<News> {
		val result = RetrofitInstance.api.searchNews(category, searchQuery,  pageNumber)
		return converter.convert(result).toDataWrapperNews()
	}

	suspend fun upsert (article:ArticlesUI.Article):Long = db.getArticleDao().upsert(article)

	suspend fun delete (article:ArticlesUI.Article) = db.getArticleDao().deleteArticle(article)

	 fun getAllSavedArticles () = db.getArticleDao().getAllArticles()

	suspend fun deleteAll()= db.getArticleDao().deleteAll()

	  suspend fun getArticle (url:String):Boolean {
		 return db.getArticleDao().getArticle(url)
		 Log.d("JIJ", "reklmflekr  " + db.getArticleDao().getArticle(url))
	 }





}