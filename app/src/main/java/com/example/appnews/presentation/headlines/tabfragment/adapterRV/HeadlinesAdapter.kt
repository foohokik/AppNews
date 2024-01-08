package com.example.appnews.presentation.headlines.tabfragment.adapterRV

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.data.dataclasses.Article
import com.example.appnews.databinding.NewsItemBinding

class HeadlinesAdapter (private val listener: ArticleListener): RecyclerView.Adapter<ArticleViewHolder>() {

    private val items = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun setItems(newItems: List<Article>) {
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

}