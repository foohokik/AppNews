package com.example.appnews.presentation.headlines.tabfragment.adapterRV

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.appnews.R
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.LoadingItemBinding
import com.example.appnews.databinding.NewsItemBinding

class HeadlinesAdapter (private val listener: ArticleListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: ViewBinding
    private lateinit var newsItemBinding: NewsItemBinding
    private lateinit var loadingItemBinding: LoadingItemBinding
    private val items = mutableListOf<ArticlesUI>()

    override fun getItemViewType(position: Int) = when (items[position]) {
        is ArticlesUI.Article -> R.layout.news_item
        is ArticlesUI.Loading -> R.layout.loading_item
        //else -> throw IllegalArgumentException("Invalid type of item $position")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
//        if (viewType==R.layout.news_item) {
//            newsItemBinding = NewsItemBinding.inflate(layoutInflater, parent, false)
//            return ArticleViewHolder(newsItemBinding)
//           } else if (viewType==R.layout.loading_item) {
//            loadingItemBinding = LoadingItemBinding.inflate(layoutInflater, parent, false)
//            return LoadingViewHolder(loadingItemBinding)
//        }

        return when (binding) {
           newsItemBinding -> ArticleViewHolder(NewsItemBinding.inflate(layoutInflater, parent, false))
          // loadingItemBinding -> LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
            else ->  LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
        }
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       // holder.bind(items[position], listener)
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
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

}