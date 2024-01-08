package com.example.appnews.presentation.headlines.tabfragment.adapterRV

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appnews.data.dataclasses.Article
import com.example.appnews.databinding.NewsItemBinding
import android.content.Context

class ArticleViewHolder (val binding: NewsItemBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind (article: Article, listener: ArticleListener) = with(binding) {

        Glide.with(ivNewsHeadline.context).load(article.urlToImage).into(ivNewsHeadline)
        tvSourceNewsName.text = article.source.name
        tvTitle.text = article.title
        root.setOnClickListener {
            listener.onClickArticle(article)
        }

    }

}