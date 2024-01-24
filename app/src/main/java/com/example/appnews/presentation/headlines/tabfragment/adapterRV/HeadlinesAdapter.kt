package com.example.appnews.presentation.headlines.tabfragment.adapterRV

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.appnews.R
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.LoadingItemBinding
import com.example.appnews.databinding.NewsItemBinding

class HeadlinesAdapter(private val listener: ArticleListener) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private val items = mutableListOf<ArticlesUI>()

	override fun getItemViewType(position: Int) = when (items[position]) {
		is ArticlesUI.Article -> TYPE_ITEM
		is ArticlesUI.Loading -> TYPE_LOADING
		else -> throw IllegalArgumentException("Invalid type of item $position")
	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)

		return when (viewType) {
			TYPE_ITEM -> ArticleViewHolder(
				NewsItemBinding.inflate(
					layoutInflater, parent,
					false
				)
			)

			TYPE_LOADING -> LoadingViewHolder(
				LoadingItemBinding.inflate(
					layoutInflater, parent,
					false
				)
			)

			else -> LoadingViewHolder(
				LoadingItemBinding.inflate(
					layoutInflater, parent,
					false
				)
			)
		}

	}

	override fun getItemCount(): Int = items.size

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is ArticleViewHolder -> holder.bind(items[position] as ArticlesUI.Article, listener)
			is LoadingViewHolder -> holder.bind(items[position] as ArticlesUI.Loading)

		}
	}

	fun setItems(newItems: List<ArticlesUI>) {
		val diffResult = DiffUtil.calculateDiff(
			ArticleDiffUtil(
				items,
				newItems
			)
		)
		items.clear()
		items.addAll(newItems)
		diffResult.dispatchUpdatesTo(this)
	}

	companion object {
		private const val TYPE_ITEM = 0
		private const val TYPE_LOADING = 1
	}

}